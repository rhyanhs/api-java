const API_URL = "http://localhost:8080"; // URL da sua API Java

// 🔹 LOGIN
const form = document.getElementById("loginForm");
if (form) {
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const usuario = document.getElementById("usuario").value;
    const senha = document.getElementById("senha").value;

    const response = await fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ usuario, senha }),
    });

    const data = await response.json();

    if (response.ok) {
      localStorage.setItem("token", data.token);
      window.location.href = "usuarios.html"; // vai pra tela de usuários
    } else {
      alert(data.error || "Falha no login");
    }
  });
}

//  CARREGAR LISTA DE USUÁRIOS
async function carregarUsuarios() {
  const token = localStorage.getItem("token");
  if (!token) {
    alert("Faça login primeiro!");
    window.location.href = "index.html";
    return;
  }

  const response = await fetch(`${API_URL}/usuarios`, {
    headers: { "Authorization": "Bearer " + token },
  });

  if (response.status === 403 || response.status === 401) {
    alert("Sessão expirada, faça login novamente.");
    localStorage.removeItem("token");
    window.location.href = "index.html";
    return;
  }

  const usuarios = await response.json();
  const tabela = document.getElementById("listaUsuarios");
  tabela.innerHTML = "";

  usuarios.forEach(u => {
    const linha = document.createElement("tr");
    linha.innerHTML = `<td>${u.id}</td><td>${u.nome}</td><td>${u.idade}</td>`;
    tabela.appendChild(linha);
  });
}

// 🔹 CRIAR NOVO USUÁRIO
async function criarUsuario() {
  const nome = document.getElementById("nome").value;
  const idade = document.getElementById("idade").value;
  const token = localStorage.getItem("token");

  const response = await fetch(`${API_URL}/usuarios`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token,
    },
    body: JSON.stringify({ nome, idade }),
  });

  if (response.ok) {
    alert("Usuário criado!");
    carregarUsuarios();
  } else {
    alert("Erro ao criar usuário");
  }
}
