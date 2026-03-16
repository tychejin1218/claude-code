import { RouterProvider } from 'react-router-dom';
import router from './app/router/router';
import DialogRenderer from './shared/components/ui/Dialog';
import ToastRenderer from './shared/components/ui/Toast';

const App = () => {
  return (
    <>
      <RouterProvider router={router} />
      <DialogRenderer />
      <ToastRenderer />
    </>
  );
};

export default App;
