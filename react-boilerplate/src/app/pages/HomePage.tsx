import { env } from '@/app/config/env';

const HomePage = () => {
  return (
    <div>
      <div>env: {env.VITE_APP_ENV}</div>
      <div>api base url: {env.VITE_API_BASE_URL}</div>
    </div>
  );
};

export default HomePage;
