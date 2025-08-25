const API_URL = "http://localhost:8080/api/users";
const AUTH_URL = "http://localhost:8080/api/auth";

export async function registerUser(userData, confirmPassword) {
  const response = await fetch(`${API_URL}/register?confirmPassword=${confirmPassword}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(userData),
  });

  const text = await response.text();
  let data;
  try { data = JSON.parse(text); } 
  catch { data = { error: 'Empty or invalid response from server' }; }

  if (!response.ok) throw new Error(data.error || "Registration failed");
  return data;
}

export async function loginUser(loginData) {
  const response = await fetch("http://localhost:8080/api/users/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(loginData),
  });

  const text = await response.text();
  let data;
  try {
    data = JSON.parse(text);
  } catch {
    data = { error: 'Empty or invalid response from server' };
  }

  if (!response.ok) {
    throw new Error(data.error || "Login failed");
  }

  return data;
}


export async function logoutUser() {
  const response = await fetch(`${AUTH_URL}/logout`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  });
  return response.ok;
}
