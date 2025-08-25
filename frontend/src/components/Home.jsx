import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';

function Home() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    // Sprawdzenie logowania przez token w localStorage
    const token = localStorage.getItem("token"); // dopasowane do LoginForm.jsx
    setIsLoggedIn(!!token);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token"); // usuń token
    setIsLoggedIn(false);
    navigate("/"); // przekierowanie bez przeładowania strony
  };

  return (
    <div className="d-flex flex-column min-vh-100">
      {/* Header */}
      <header className="bg-dark text-white py-3">
        <div className="container-fluid d-flex justify-content-between align-items-center">
          <h1 className="h3 mb-0">DrumkitTagger</h1>
          <nav>
            {isLoggedIn ? (
              <button className="btn btn-outline-light" onClick={handleLogout}>
                Logout
              </button>
            ) : (
              <>
                <a href="/login" className="text-white me-3">Login</a>
                <a href="/register" className="text-white">Register</a>
              </>
            )}
          </nav>
        </div>
      </header>

      {/* Main content */}
      <main className="flex-grow-1 d-flex justify-content-center align-items-center">
        <div className="text-center px-3">
          <h2 className="mb-3">Welcome to DrumkitTagger</h2>
          <p className="text-muted">
            Upload your drumkit, add buyer’s nickname and get a tagged version with a unique 5-digit ID.
          </p>
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-light text-center py-3 mt-auto">
        © {new Date().getFullYear()} DrumkitTagger
      </footer>
    </div>
  );
}

export default Home;
