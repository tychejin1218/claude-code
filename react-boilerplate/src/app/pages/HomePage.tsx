const HomePage = () => {
  return (
    <div>
      <div>env: {import.meta.env.VITE_APP_ENV}</div>
      <div>api base url: {import.meta.env.VITE_API_BASE_URL}</div>
    </div>
  );
};

export default HomePage;
