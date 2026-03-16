import { useState } from 'react';
import { useTodos, useCreateTodo, useCompleteTodo, useDeleteTodo } from '@/features/todo/hooks/useTodos';
import TodoItem from '@/features/todo/components/TodoItem';

const TodoPage = () => {
  const [input, setInput] = useState('');

  const { data: todos, isLoading } = useTodos();
  const createTodo = useCreateTodo();
  const completeTodo = useCompleteTodo();
  const deleteTodo = useDeleteTodo();

  const handleAdd = (e: React.FormEvent) => {
    e.preventDefault();
    const title = input.trim();
    if (!title) return;
    createTodo.mutate({ title });
    setInput('');
  };

  return (
    <div className="mx-auto max-w-lg py-6">
      <h2 className="mb-4 text-xl font-bold text-gray-800">할 일 목록</h2>

      <form onSubmit={handleAdd} className="mb-4 flex gap-2">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="새 할 일 입력"
          className="flex-1 rounded border border-gray-300 px-3 py-2 text-sm focus:border-blue-500 focus:outline-none"
        />
        <button
          type="submit"
          disabled={createTodo.isPending}
          className="rounded bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700 disabled:opacity-50"
        >
          추가
        </button>
      </form>

      {isLoading && <p className="text-sm text-gray-400">불러오는 중...</p>}

      {todos && todos.length === 0 && (
        <p className="text-sm text-gray-400">할 일이 없습니다. 추가해보세요!</p>
      )}

      <ul className="flex flex-col gap-2">
        {todos?.map((todo) => (
          <TodoItem
            key={todo.id}
            todo={todo}
            onComplete={(id) => completeTodo.mutate(id)}
            onDelete={(id) => deleteTodo.mutate(id)}
          />
        ))}
      </ul>
    </div>
  );
};

export default TodoPage;
