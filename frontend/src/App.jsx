import { useState } from "react";
import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import Home from "./components/Home";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import { loginUser, logoutUser } from "./services/api";

function App() {
  const [currentUser, setCurrentUser] = useState(null);

  const handleLogin = async (credentials, navigate) => {
    try {
      const data = await loginUser(credentials);
      setCurrentUser(data.user);
      navigate("/"); // przekierowanie na stronę główną
    } catch (err) {
      alert(err.message);
    }
  };

  const handleLogout = async () => {
    await logoutUser();
    setCurrentUser(null);
  };

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home user={currentUser} onLogout={handleLogout} />} />
        <Route path="/login" element={<LoginForm onLogin={handleLogin} />} />
        <Route path="/register" element={<RegisterForm onRegister={handleLogin} />} />
      </Routes>
    </Router>
  );
}

export default App;
