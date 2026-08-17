/**
 * Discodeit 프론트엔드
 * 백엔드 REST API (/api/**) 를 그대로 호출하는 순수 JS SPA
 */

/* ========================= API 헬퍼 ========================= */

const API = {
  async request(url, options = {}) {
    const res = await fetch(url, {
      headers: { "Content-Type": "application/json" },
      ...options,
    });
    if (!res.ok) {
      let msg = `요청 실패 (${res.status})`;
      try {
        const body = await res.json();
        if (body && body.message) msg = body.message;
      } catch (_) { /* 본문이 없거나 JSON이 아니면 무시 */ }
      throw new Error(msg);
    }
    const text = await res.text();
    return text ? JSON.parse(text) : null;
  },

  // Auth
  login(username, password) {
    return this.request("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password }),
    });
  },

  // User
  createUser(userName, email, password, data) {
    return this.request("/api/users", {
      method: "POST",
      body: JSON.stringify({ userName, email, password, data }),
    });
  },
  getUsers() {
    return this.request("/api/users");
  },
  updateUserStatus(userId) {
    return this.request(`/api/users/${userId}/status`, { method: "PATCH" });
  },

  // Channel
  getChannels(userId) {
    return this.request(`/api/channels/${userId}`);
  },
  createChannel(channelType, title, memo, userIds) {
    return this.request("/api/channels", {
      method: "POST",
      body: JSON.stringify({ channelType, title, memo, userIds }),
    });
  },
  updateChannel(id, channelType, title, memo, userIds) {
    return this.request(`/api/channels/${id}`, {
      method: "PATCH",
      body: JSON.stringify({ channelType, title, memo, userIds }),
    });
  },
  deleteChannel(id) {
    return this.request(`/api/channels/${id}`, { method: "DELETE" });
  },

  // Message
  getMessages(channelId) {
    return this.request(`/api/messages/${channelId}`);
  },
  createMessage(message, channelId, userId, attachmentIds = []) {
    return this.request("/api/messages", {
      method: "POST",
      body: JSON.stringify({ attachmentIds, message, channelId, userId }),
    });
  },
  updateMessage(id, message, channelId, authorId, attachmentIds = []) {
    return this.request(`/api/messages/${id}`, {
      method: "PATCH",
      body: JSON.stringify({ message, channelId, authorId, attachmentIds }),
    });
  },
  deleteMessage(id) {
    return this.request(`/api/messages/${id}`, { method: "DELETE" });
  },

  // BinaryContent
  getBinaryContents(ids) {
    const params = ids.map((id) => `ids=${encodeURIComponent(id)}`).join("&");
    return this.request(`/api/binary?${params}`);
  },
};

/* ========================= 전역 상태 ========================= */

const state = {
  me: null,            // { id, userName, email, profileId }
  users: [],           // UserResponseDto[]
  channels: [],        // ChannelResponseDto[]
  currentChannel: null,
  messages: [],
  editingChannelId: null,
  binaryCache: {},     // binaryId -> dataURL
  timers: [],
};

/* ========================= DOM ========================= */

const $ = (id) => document.getElementById(id);

const authScreen = $("auth-screen");
const mainScreen = $("main-screen");
const loginForm = $("login-form");
const registerForm = $("register-form");
const channelModal = $("channel-modal");

/* ========================= 유틸 ========================= */

function isOnlineOf(user) {
  // Jackson 직렬화 방식에 따라 isOnline 또는 online 으로 내려올 수 있음
  return user.isOnline ?? user.online ?? false;
}

function fileToBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => {
      // "data:image/png;base64,xxxx" 에서 base64 부분만 추출 (byte[] 역직렬화용)
      resolve(String(reader.result).split(",")[1]);
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}

function initials(name) {
  return (name || "?").trim().charAt(0).toUpperCase();
}

function userNameOf(userId) {
  const user = state.users.find((u) => u.id === userId);
  return user ? user.userName : "(알 수 없음)";
}

async function binaryToDataUrl(binaryId) {
  if (!binaryId) return null;
  if (state.binaryCache[binaryId]) return state.binaryCache[binaryId];
  try {
    const list = await API.getBinaryContents([binaryId]);
    const found = (list || []).find((b) => b.id === binaryId) || (list || [])[0];
    if (!found || !found.data) return null;
    const url = `data:image/*;base64,${found.data}`;
    state.binaryCache[binaryId] = url;
    return url;
  } catch (_) {
    return null;
  }
}

function renderAvatar(el, user) {
  el.textContent = initials(user.userName);
  if (user.profileId) {
    binaryToDataUrl(user.profileId).then((url) => {
      if (url) el.innerHTML = `<img src="${url}" alt="" />`;
    });
  }
}

/* ========================= 인증 ========================= */

$("show-register").addEventListener("click", (e) => {
  e.preventDefault();
  loginForm.classList.add("hidden");
  registerForm.classList.remove("hidden");
});

$("show-login").addEventListener("click", (e) => {
  e.preventDefault();
  registerForm.classList.add("hidden");
  loginForm.classList.remove("hidden");
});

loginForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  $("login-error").textContent = "";
  const username = $("login-username").value.trim();
  const password = $("login-password").value;
  try {
    const loginRes = await API.login(username, password);
    // 로그인 응답에는 id가 없으므로 전체 유저 목록에서 내 정보를 찾는다
    const users = await API.getUsers();
    const me = users.find((u) => u.userName === loginRes.username);
    if (!me) throw new Error("로그인한 사용자 정보를 찾을 수 없습니다.");
    state.me = me;
    state.users = users;
    await API.updateUserStatus(me.id).catch(() => {});
    enterMain();
  } catch (err) {
    $("login-error").textContent = err.message;
  }
});

registerForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  $("register-error").textContent = "";
  const userName = $("register-username").value.trim();
  const email = $("register-email").value.trim();
  const password = $("register-password").value;
  const file = $("register-profile").files[0];
  try {
    const data = file ? await fileToBase64(file) : null;
    await API.createUser(userName, email, password, data);
    alert("회원가입 완료! 로그인해주세요.");
    registerForm.reset();
    registerForm.classList.add("hidden");
    loginForm.classList.remove("hidden");
    $("login-username").value = userName;
  } catch (err) {
    $("register-error").textContent = err.message;
  }
});

$("logout-btn").addEventListener("click", () => {
  state.timers.forEach(clearInterval);
  state.timers = [];
  state.me = null;
  state.currentChannel = null;
  mainScreen.classList.add("hidden");
  authScreen.classList.remove("hidden");
  loginForm.reset();
});

/* ========================= 메인 화면 진입 ========================= */

function enterMain() {
  authScreen.classList.add("hidden");
  mainScreen.classList.remove("hidden");

  $("me-name").textContent = state.me.userName;
  $("me-email").textContent = state.me.email;
  renderAvatar($("me-avatar"), state.me);

  refreshUsers();
  refreshChannels();

  // 주기적 갱신 (메시지 3초, 유저 10초, 채널 15초, 접속상태 30초)
  state.timers.push(setInterval(refreshMessages, 3000));
  state.timers.push(setInterval(refreshUsers, 10000));
  state.timers.push(setInterval(refreshChannels, 15000));
  state.timers.push(
    setInterval(() => API.updateUserStatus(state.me.id).catch(() => {}), 30000)
  );
}

/* ========================= 유저 목록 ========================= */

async function refreshUsers() {
  try {
    state.users = await API.getUsers();
  } catch (_) {
    return;
  }
  const list = $("user-list");
  list.innerHTML = "";
  state.users.forEach((user) => {
    const li = document.createElement("li");

    const avatar = document.createElement("div");
    avatar.className = "avatar small";
    renderAvatar(avatar, user);

    const nameCol = document.createElement("div");
    nameCol.className = "user-name-col";
    const nameEl = document.createElement("span");
    nameEl.textContent =
      user.id === state.me.id ? `${user.userName} (나)` : user.userName;
    const emailEl = document.createElement("span");
    emailEl.className = "sub";
    emailEl.textContent = user.email;
    nameCol.append(nameEl, emailEl);

    const dot = document.createElement("span");
    dot.className = `status-dot ${isOnlineOf(user) ? "online" : "offline"}`;
    dot.title = isOnlineOf(user) ? "온라인" : "오프라인";

    li.append(avatar, nameCol, dot);
    list.appendChild(li);
  });
}

/* ========================= 채널 ========================= */

async function refreshChannels() {
  try {
    state.channels = await API.getChannels(state.me.id);
  } catch (_) {
    return;
  }
  const list = $("channel-list");
  list.innerHTML = "";
  state.channels.forEach((ch) => {
    const li = document.createElement("li");
    if (state.currentChannel && state.currentChannel.id === ch.id) {
      li.classList.add("active");
    }
    const badge = document.createElement("span");
    badge.className = "channel-badge";
    badge.textContent = ch.channelType === "PRIVATE" ? "🔒" : "#";
    const title = document.createElement("span");
    title.textContent = ch.title || "(이름 없음)";
    li.append(badge, title);
    li.addEventListener("click", () => selectChannel(ch));
    list.appendChild(li);
  });

  // 선택 중인 채널이 삭제됐으면 화면 초기화
  if (
    state.currentChannel &&
    !state.channels.some((c) => c.id === state.currentChannel.id)
  ) {
    clearChannelView();
  }
}

function selectChannel(channel) {
  state.currentChannel = channel;
  $("current-channel-title").textContent =
    (channel.channelType === "PRIVATE" ? "🔒 " : "# ") + (channel.title || "");
  $("current-channel-memo").textContent = channel.memo || "";
  $("message-form").classList.remove("hidden");
  $("edit-channel-btn").classList.remove("hidden");
  $("delete-channel-btn").classList.remove("hidden");
  refreshChannels();
  refreshMessages();
}

function clearChannelView() {
  state.currentChannel = null;
  state.messages = [];
  $("current-channel-title").textContent = "채널을 선택하세요";
  $("current-channel-memo").textContent = "";
  $("message-form").classList.add("hidden");
  $("edit-channel-btn").classList.add("hidden");
  $("delete-channel-btn").classList.add("hidden");
  $("message-list").innerHTML =
    '<p class="placeholder">좌측에서 채널을 선택하면 메시지가 표시됩니다.</p>';
}

$("delete-channel-btn").addEventListener("click", async () => {
  if (!state.currentChannel) return;
  if (!confirm(`'${state.currentChannel.title}' 채널을 삭제할까요?`)) return;
  try {
    await API.deleteChannel(state.currentChannel.id);
    clearChannelView();
    refreshChannels();
  } catch (err) {
    alert(err.message);
  }
});

/* ===== 채널 모달 ===== */

function openChannelModal(channel = null) {
  state.editingChannelId = channel ? channel.id : null;
  $("channel-modal-title").textContent = channel ? "채널 수정" : "채널 만들기";
  $("channel-type").value = channel ? channel.channelType : "PUBLIC";
  $("channel-title").value = channel ? channel.title || "" : "";
  $("channel-memo").value = channel ? channel.memo || "" : "";
  $("channel-error").textContent = "";

  // 참여자 체크박스 (본인은 항상 포함, 해제 불가)
  const box = $("channel-user-checks");
  box.innerHTML = "";
  const checkedIds = channel ? channel.userIds || [] : [state.me.id];
  state.users.forEach((user) => {
    const label = document.createElement("label");
    const cb = document.createElement("input");
    cb.type = "checkbox";
    cb.value = user.id;
    cb.checked = user.id === state.me.id || checkedIds.includes(user.id);
    if (user.id === state.me.id) cb.disabled = true;
    label.append(
      cb,
      document.createTextNode(
        user.id === state.me.id ? `${user.userName} (나)` : user.userName
      )
    );
    box.appendChild(label);
  });

  channelModal.classList.remove("hidden");
}

$("open-channel-modal").addEventListener("click", () => openChannelModal());
$("edit-channel-btn").addEventListener("click", () => {
  if (state.currentChannel) openChannelModal(state.currentChannel);
});
$("channel-cancel").addEventListener("click", () =>
  channelModal.classList.add("hidden")
);

$("channel-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  const channelType = $("channel-type").value;
  const title = $("channel-title").value.trim();
  const memo = $("channel-memo").value.trim();
  const userIds = [
    ...$("channel-user-checks").querySelectorAll("input:checked"),
  ].map((cb) => cb.value);
  if (!userIds.includes(state.me.id)) userIds.push(state.me.id);

  try {
    if (state.editingChannelId) {
      const updated = await API.updateChannel(
        state.editingChannelId, channelType, title, memo, userIds
      );
      if (state.currentChannel && state.currentChannel.id === updated.id) {
        selectChannel(updated);
      }
    } else {
      const created = await API.createChannel(channelType, title, memo, userIds);
      selectChannel(created);
    }
    channelModal.classList.add("hidden");
    refreshChannels();
  } catch (err) {
    $("channel-error").textContent = err.message;
  }
});

/* ========================= 메시지 ========================= */

async function refreshMessages() {
  if (!state.currentChannel) return;
  let messages;
  try {
    messages = await API.getMessages(state.currentChannel.id);
  } catch (_) {
    return;
  }
  state.messages = messages || [];
  renderMessages();
}

function renderMessages() {
  const list = $("message-list");
  const nearBottom =
    list.scrollHeight - list.scrollTop - list.clientHeight < 60;
  list.innerHTML = "";

  if (state.messages.length === 0) {
    list.innerHTML = '<p class="placeholder">아직 메시지가 없습니다.</p>';
    return;
  }

  state.messages.forEach((msg) => {
    const item = document.createElement("div");
    item.className = "message-item";

    const author = state.users.find((u) => u.id === msg.userId);
    const avatar = document.createElement("div");
    avatar.className = "avatar";
    renderAvatar(avatar, author || { userName: "?" });

    const body = document.createElement("div");
    body.className = "message-body";

    const meta = document.createElement("div");
    meta.className = "message-meta";
    const name = document.createElement("span");
    name.className = "message-author";
    name.textContent = userNameOf(msg.userId);
    meta.appendChild(name);

    // 내 메시지에만 수정/삭제 버튼
    if (msg.userId === state.me.id) {
      const actions = document.createElement("span");
      actions.className = "message-actions";

      const editBtn = document.createElement("button");
      editBtn.className = "btn icon small";
      editBtn.textContent = "✏";
      editBtn.title = "수정";
      editBtn.addEventListener("click", () => editMessage(msg));

      const delBtn = document.createElement("button");
      delBtn.className = "btn icon small";
      delBtn.textContent = "🗑";
      delBtn.title = "삭제";
      delBtn.addEventListener("click", () => removeMessage(msg));

      actions.append(editBtn, delBtn);
      meta.appendChild(actions);
    }

    const text = document.createElement("div");
    text.className = "message-text";
    text.textContent = msg.message;

    body.append(meta, text);

    // 첨부 이미지 표시
    if (msg.attachmentIds && msg.attachmentIds.length > 0) {
      const attach = document.createElement("div");
      attach.className = "message-attachments";
      msg.attachmentIds.forEach((binaryId) => {
        binaryToDataUrl(binaryId).then((url) => {
          if (url) {
            const img = document.createElement("img");
            img.src = url;
            attach.appendChild(img);
          }
        });
      });
      body.appendChild(attach);
    }

    item.append(avatar, body);
    list.appendChild(item);
  });

  if (nearBottom) list.scrollTop = list.scrollHeight;
}

$("message-form").addEventListener("submit", async (e) => {
  e.preventDefault();
  if (!state.currentChannel) return;
  const input = $("message-input");
  const content = input.value.trim();
  if (!content) return;
  try {
    await API.createMessage(content, state.currentChannel.id, state.me.id);
    input.value = "";
    await refreshMessages();
    const list = $("message-list");
    list.scrollTop = list.scrollHeight;
  } catch (err) {
    alert(err.message);
  }
});

async function editMessage(msg) {
  const next = prompt("메시지 수정", msg.message);
  if (next === null || next.trim() === "" || next === msg.message) return;
  try {
    await API.updateMessage(
      msg.id, next.trim(), msg.channelId, msg.userId, msg.attachmentIds || []
    );
    refreshMessages();
  } catch (err) {
    alert(err.message);
  }
}

async function removeMessage(msg) {
  if (!confirm("메시지를 삭제할까요?")) return;
  try {
    await API.deleteMessage(msg.id);
    refreshMessages();
  } catch (err) {
    alert(err.message);
  }
}
