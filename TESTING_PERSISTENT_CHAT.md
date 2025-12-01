# Testing Persistent Chat Feature

This document describes how to test the newly implemented persistent chat feature.

## Prerequisites

1. PostgreSQL database running (via Docker or locally)
2. Environment variables configured
3. Backend application built and ready to run
4. Frontend files accessible via a web server

## Setup Steps

### 1. Database Migration

The new migration file `V4__create_chat_tables.sql` will run automatically when you start the backend application. It creates:
- `chat_conversation` table (stores conversations with timestamps)
- `chat_message` table (stores individual messages)
- Necessary indexes for performance

### 2. Start the Database

```bash
docker-compose up -d postgres
```

### 3. Start the Backend

```bash
cd backend
./gradlew bootRun
```

Or if you have environment variables in a `.env` file, source them first:

```bash
source .env  # or source example.env if using that
cd backend
./gradlew bootRun
```

The backend should start on `http://localhost:8080`.

### 4. Serve the Frontend

Open the frontend in a browser. You can use a simple HTTP server:

```bash
cd frontend/src
python3 -m http.server 3000
```

Then navigate to `http://localhost:3000/html/chat.html`.

## Testing Checklist

### Backend API Tests

You can test the backend endpoints using curl:

1. **Create a new conversation:**
```bash
curl -X POST http://localhost:8080/chat/new
# Expected response: {"conversationId": 1}
```

2. **Send a message:**
```bash
curl -X POST http://localhost:8080/chat \
  -H "Content-Type: application/json" \
  -d '{"conversationId": 1, "message": "Hallo, ich brauche Hilfe bei einem Förderantrag"}'
# Expected response: {"reply": "...AI response..."}
```

3. **List all conversations:**
```bash
curl http://localhost:8080/chat
# Expected response: [{"id": 1, "updatedAt": "2025-12-01T..."}]
```

4. **Get messages from a conversation:**
```bash
curl http://localhost:8080/chat/1/messages
# Expected response: [{"role": "user", "content": "...", "createdAt": "..."}, ...]
```

### Frontend UI Tests

1. **Initial Load:**
   - Open `http://localhost:3000/html/chat.html`
   - The chat list on the left should be empty (if first time)
   - Chat area should be empty

2. **First Message (Auto-create conversation):**
   - Type a message in the input field
   - Click "Senden" button
   - A new conversation should be created automatically
   - User message appears in chat log
   - Assistant reply appears after a moment
   - Chat list on the left shows the new conversation with timestamp

3. **Continue Existing Chat:**
   - Send another message
   - Both messages should appear in the chat log
   - Timestamp in the chat list should update

4. **New Chat Button:**
   - Click "+ Neuer Chat" button
   - Chat log should clear
   - A new empty conversation is created
   - Send a message to start the new conversation
   - Both conversations should now appear in the list

5. **Switch Between Chats:**
   - Click on a conversation in the left sidebar
   - Chat log should load all messages from that conversation
   - The selected conversation should be highlighted
   - Send a new message to continue the conversation
   - Timestamp should update

6. **Timestamp Display:**
   - Check that timestamps show as:
     - "Gerade eben" for messages just sent
     - "vor X Min." for recent messages
     - "vor X Std." for hours ago
     - "vor X Tag(en)" for days ago
     - Date format (DD.MM.YYYY) for older messages

7. **Multiple Conversations:**
   - Create 3-4 different conversations
   - Verify they all appear in the list sorted by most recent
   - Verify you can switch between them and see correct messages
   - Verify timestamps update correctly when you send new messages

8. **Persistence Test:**
   - Create a conversation and send several messages
   - Refresh the page (F5)
   - Verify the conversation list still shows all conversations
   - Verify clicking on a conversation loads all its messages

## Expected Behavior

### ChatGPT-like Interface:
- ✅ Left sidebar with list of conversations
- ✅ Each conversation shows timestamp of last message
- ✅ Click on conversation to load and continue it
- ✅ "New Chat" button to start fresh conversation
- ✅ Active conversation highlighted in list

### Database Persistence:
- ✅ All conversations saved to database
- ✅ All messages saved to database
- ✅ Conversations survive page refresh
- ✅ Conversations sorted by most recent update

### API Changes:
- ✅ Frontend sends only new message (not full history)
- ✅ Backend loads history from database
- ✅ Backend saves both user and assistant messages
- ✅ Conversation timestamp updates on new message

## Common Issues and Solutions

### Issue: "Conversation not found" error
- **Cause:** Trying to send message to non-existent conversation
- **Solution:** Frontend should auto-create conversation if none exists

### Issue: Empty chat list after refresh
- **Cause:** Database not running or connection issue
- **Solution:** Check database is running and connection string is correct

### Issue: Messages not persisting
- **Cause:** Migration not run or transaction not committed
- **Solution:** Check backend logs for migration status

### Issue: CORS errors in browser console
- **Cause:** Frontend and backend on different origins
- **Solution:** WebConfig already allows all origins, verify backend is running

## Files Changed

### Backend:
- `backend/src/main/resources/db/migration/V4__create_chat_tables.sql` - New migration
- `backend/src/main/java/at/fhtw/grantscout/out/persistence/entities/ChatConversation.java` - New entity
- `backend/src/main/java/at/fhtw/grantscout/out/persistence/entities/ChatMessage.java` - New entity
- `backend/src/main/java/at/fhtw/grantscout/out/persistence/repositories/ChatConversationRepository.java` - New repository
- `backend/src/main/java/at/fhtw/grantscout/out/persistence/repositories/ChatMessageRepository.java` - New repository
- `backend/src/main/java/at/fhtw/grantscout/core/ChatService.java` - Updated to use database
- `backend/src/main/java/at/fhtw/grantscout/in/rest/ChatController.java` - Added new endpoints
- `backend/src/main/java/at/fhtw/grantscout/core/domain/data/chat/ChatRequest.java` - Updated structure
- `backend/src/main/java/at/fhtw/grantscout/core/domain/data/chat/ConversationDto.java` - New DTO
- `backend/src/main/java/at/fhtw/grantscout/core/domain/data/chat/MessageDto.java` - New DTO
- `backend/src/main/resources/application.yaml` - Added JPA configuration

### Frontend:
- `frontend/src/html/chat.html` - Updated layout with sidebar
- `frontend/src/css/chat.css` - Updated styles for new layout
- `frontend/src/js/chat.js` - New file with persistent chat logic

## Success Criteria

The feature is working correctly if:
1. ✅ Conversations are saved to database and persist across page refreshes
2. ✅ Chat list shows all conversations sorted by most recent
3. ✅ Can create new conversations and continue existing ones
4. ✅ Messages load correctly when selecting a conversation
5. ✅ Timestamps display in human-readable format
6. ✅ UI matches ChatGPT-style interface with sidebar
7. ✅ No errors in browser console or backend logs
8. ✅ API sends only new messages, backend manages full history

## Next Steps

After successful testing, consider:
- Adding conversation titles (auto-generated from first message)
- Adding delete conversation functionality
- Adding edit/regenerate message features
- Adding conversation search/filter
- Adding pagination for large conversation lists

