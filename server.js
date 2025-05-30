// firebase.js
import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import {
  getDatabase,
  ref,
  push,
  onValue
} from "https://www.gstatic.com/firebasejs/10.12.0/firebase-database.js";

// Configuración de Firebase (la que me diste)
const firebaseConfig = {
  databaseURL: "https://digitalizacion-966d1-default-rtdb.europe-west1.firebasedatabase.app/",
  apiKey: "AIzaSyA_8kLupGg5pR8qEoMY8c9aNgHeKqtxCF8",
  authDomain: "digitalizacionproyecto-a6e81.firebaseapp.com",
  projectId: "digitalizacionproyecto-a6e81",
  storageBucket: "digitalizacionproyecto-a6e81.firebasestorage.app",
  messagingSenderId: "591813058138",
  appId: "1:591813058138:web:a385ac493a8a0dab92c263"
};

// Inicializar Firebase y la base de datos
const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

// Elementos del DOM
const form = document.getElementById("userForm");
const userList = document.getElementById("userList");

// Guardar usuario en Firebase
form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;

  const usersRef = ref(db, "usuarios");
  await push(usersRef, { name, email });

  form.reset();
});

// Cargar usuarios en tiempo real
const usersRef = ref(db, "usuarios");
onValue(usersRef, (snapshot) => {
  userList.innerHTML = "";
  snapshot.forEach((child) => {
    const user = child.val();
    const li = document.createElement("li");
    li.textContent = `${user.name} - ${user.email}`;
    userList.appendChild(li);
  });
});

