<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>README - Registro de Usuarios con Firebase</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      line-height: 1.6;
      margin: 2rem;
      background-color: #f9f9f9;
    }
    code, pre {
      background-color: #eee;
      padding: 0.5rem;
      display: block;
      white-space: pre-wrap;
      border-radius: 5px;
      overflow-x: auto;
    }
    h1, h2, h3 {
      color: #333;
    }
    ul {
      list-style-type: disc;
      padding-left: 2rem;
    }
  </style>
</head>
<body>

  <h1>📋 Registro de Usuarios con Firebase</h1>
  <p>Este proyecto es una aplicación web sencilla que permite registrar usuarios mediante un formulario HTML y guardar la información en Firebase Firestore. Los usuarios registrados se muestran automáticamente en una lista.</p>

  <h2>🚀 Tecnologías Utilizadas</h2>
  <ul>
    <li>HTML5</li>
    <li>JavaScript (ES Modules)</li>
    <li>Firebase (Cloud Firestore)</li>
  </ul>

  <h2>🛠️ Configuración del Proyecto</h2>

  <h3>1. Clona o descarga este repositorio</h3>
  <pre><code>git clone https://github.com/tu-usuario/registro-usuarios-firebase.git</code></pre>

  <h3>2. Estructura del Proyecto</h3>
  <pre><code>
registro-usuarios-firebase/
├── index.html
├── firebase.js
└── README.md
  </code></pre>

  <h3>3. Agrega tu configuración de Firebase</h3>
  <p>Edita el archivo <code>firebase.js</code> y reemplaza la configuración con los datos de tu proyecto de Firebase:</p>

  <pre><code>
const firebaseConfig = {
  apiKey: "TU_API_KEY",
  authDomain: "TU_AUTH_DOMAIN",
  projectId: "TU_PROJECT_ID",
  storageBucket: "TU_STORAGE_BUCKET",
  messagingSenderId: "TU_MESSAGING_SENDER_ID",
  appId: "TU_APP_ID"
};
  </code></pre>

  <p>Puedes obtener estos datos en la <a href="https://console.firebase.google.com/" target="_blank">Consola de Firebase</a>.</p>

  <h2>🧪 Cómo Probarlo Localmente</h2>
  <ol>
    <li>Abre el proyecto en tu editor favorito (por ejemplo, VSCode).</li>
    <li>Usa una extensión como <strong>Live Server</strong> o abre <code>index.html</code> directamente en tu navegador.</li>
    <li>Completa el formulario con un nombre y correo electrónico.</li>
    <li>Verás los usuarios registrados en la lista debajo del formulario.</li>
  </ol>

  <h2>🔐 Configuración de Seguridad</h2>
  <p>Durante el desarrollo, asegúrate de que las reglas de seguridad de Firestore estén en <strong>modo de prueba</strong>:</p>

  <pre><code>
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
  </code></pre>

  <p><strong>⚠️ No uses estas reglas en producción.</strong></p>

  <h2>✅ Funcionalidades</h2>
  <ul>
    <li>Registro de usuarios (nombre + correo)</li>
    <li>Almacenamiento en Firestore</li>
    <li>Lectura y visualización de usuarios registrados</li>
    <li>Recarga automática de lista tras nuevo registro</li>
  </ul>

  <h2>📦 Recursos</h2>
  <ul>
    <li><a href="https://console.firebase.google.com/" target="_blank">Firebase Console</a></li>
    <li><a href="https://firebase.google.com/docs/firestore" target="_blank">Documentación de Firebase Firestore</a></li>
  </ul>

</body>
</html>
