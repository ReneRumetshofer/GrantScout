const API = 'http://localhost:8080/chat';
const log = document.getElementById('log');
const form = document.getElementById('f');
const q = document.getElementById('q');
const chatList = document.getElementById('chat-list');
const newChatBtn = document.getElementById('new-chat-btn');

let currentConversationId = null;

// Helper: format time as relative (e.g., "2 minutes ago")
function formatRelativeTime(timestamp) {
    const now = new Date();
    const date = new Date(timestamp);
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffMins < 1) return 'Gerade eben';
    if (diffMins < 60) return `vor ${diffMins} Min.`;
    if (diffHours < 24) return `vor ${diffHours} Std.`;
    if (diffDays < 7) return `vor ${diffDays} Tag${diffDays > 1 ? 'en' : ''}`;
    
    // Format as date if older than a week
    return date.toLocaleDateString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric' });
}

// Add message bubble to chat log
function push(role, content) {
    const div = document.createElement('div');
    div.className = 'msg ' + role;
    div.innerHTML = `<div class="role">${role}</div><div class="bubble"></div>`;
    div.querySelector('.bubble').textContent = content;
    log.appendChild(div);
    log.scrollTop = log.scrollHeight;
}

// Clear chat log
function clearLog() {
    log.innerHTML = '';
}

// Load and display all conversations
async function loadConversations() {
    try {
        const res = await fetch(API);
        const conversations = await res.json();
        
        chatList.innerHTML = '';
        conversations.forEach(conv => {
            const item = document.createElement('div');
            item.className = 'chat-list-item';
            if (conv.id === currentConversationId) {
                item.classList.add('active');
            }
            item.innerHTML = `<div class="chat-list-item-time">${formatRelativeTime(conv.updatedAt)}</div>`;
            item.addEventListener('click', () => selectConversation(conv.id, item));
            chatList.appendChild(item);
        });
    } catch (err) {
        console.error('Failed to load conversations:', err);
    }
}

// Select and load a conversation
async function selectConversation(conversationId, itemElement) {
    currentConversationId = conversationId;
    clearLog();
    
    // Update active state in list
    document.querySelectorAll('.chat-list-item').forEach(item => {
        item.classList.remove('active');
    });
    if (itemElement) {
        itemElement.classList.add('active');
    }
    
    // Load messages for this conversation
    try {
        const res = await fetch(`${API}/${conversationId}/messages`);
        const messages = await res.json();
        messages.forEach(msg => {
            push(msg.role, msg.content);
        });
    } catch (err) {
        console.error('Failed to load messages:', err);
    }
}

// Create a new conversation
async function createNewConversation() {
    try {
        const res = await fetch(`${API}/new`, { method: 'POST' });
        const data = await res.json();
        currentConversationId = data.conversationId;
        clearLog();
        await loadConversations();
    } catch (err) {
        console.error('Failed to create conversation:', err);
    }
}

// Send a message
async function sendMessage(text) {
    // If no conversation is active, create one
    if (!currentConversationId) {
        await createNewConversation();
    }
    
    push('user', text);
    form.send.disabled = true;
    
    try {
        const res = await fetch(API, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                conversationId: currentConversationId, 
                message: text 
            })
        });
        const data = await res.json();
        push('assistant', data.reply);
        
        // Refresh conversation list to update timestamp
        await loadConversations();
    } catch (err) {
        console.error('Failed to send message:', err);
        push('assistant', 'Fehler beim Senden der Nachricht.');
    } finally {
        form.send.disabled = false;
    }
}

// Form submit handler
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const text = q.value.trim();
    if (!text) return;
    q.value = '';
    await sendMessage(text);
});

// New chat button handler
newChatBtn.addEventListener('click', createNewConversation);

// Load conversations on page load
loadConversations();

