import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
import {
  getDatabase,
  ref,
  push,
  onValue
} from "https://www.gstatic.com/firebasejs/10.12.0/firebase-database.js";

// Configuración Firebase
// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyDpuz0dKJBPRDgKFVsE1hybCiR7J9BlCuA",
  authDomain: "dddsd-bc614.firebaseapp.com",
  projectId: "dddsd-bc614",
  storageBucket: "dddsd-bc614.firebasestorage.app",
  messagingSenderId: "232008592233",
  appId: "1:232008592233:web:0aed262ed6288b12836bf8"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Inicializar Firebase
const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

// Esperar que el DOM esté cargado antes de manipular elementos
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("userForm");
  const userList = document.getElementById("userList");
  const usersRef = ref(db, "usuarios");

  // Guardar usuario en Firebase al enviar formulario
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();

    if(name && email){
      await push(usersRef, { name, email });
      form.reset();
    }
  });

  // Escuchar en tiempo real cambios en la base de datos
  onValue(usersRef, (snapshot) => {
    userList.innerHTML = "";
    snapshot.forEach((child) => {
      const user = child.val();
      const li = document.createElement("li");
      li.textContent = `${user.name} - ${user.email}`;
      userList.appendChild(li);
    });
  });
});

