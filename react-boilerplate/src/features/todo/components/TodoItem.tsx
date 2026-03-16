import type { Todo } from '@/features/todo/types/todo';

interface Props {
  todo: Todo;
  onComplete: (id: number) => void;
  onDelete: (id: number) => void;
}

const TodoItem = ({ todo, onComplete, onDelete }: Props) => {
  return (
    <li className="flex items-center gap-3 rounded border border-gray-200 bg-white px-4 py-3">
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={() => !todo.completed && onComplete(todo.id)}
        disabled={todo.completed}
        className="size-4 cursor-pointer accent-blue-600 disabled:cursor-default"
      />
      <span
        className={`flex-1 text-sm ${todo.completed ? 'text-gray-400 line-through' : 'text-gray-800'}`}
      >
        {todo.title}
      </span>
      <button
        onClick={() => onDelete(todo.id)}
        className="text-xs text-red-400 hover:text-red-600"
      >
        삭제
      </button>
    </li>
  );
};

export default TodoItem;
