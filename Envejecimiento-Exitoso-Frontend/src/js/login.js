// login.js

document.addEventListener('DOMContentLoaded', () => {
  const loginForm = document.querySelector('form');
  const registerBtn = document.querySelector('.register-btn');

  // Simulación de usuarios registrados (puede reemplazarse por una llamada real a backend)
  const usuariosRegistrados = [
    { email: 'usuario@example.com', password: '123456' },
    { email: 'admin@envejecimiento.com', password: 'admin123' }
  ];

  // Manejo del formulario de login
  loginForm.addEventListener('submit', (event) => {
    event.preventDefault(); // Prevenir envío real

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;

    const usuarioValido = usuariosRegistrados.find(
      user => user.email === email && user.password === password
    );

    if (usuarioValido) {
      // Guardar sesión simulada
      localStorage.setItem('usuario', JSON.stringify({ email }));

      alert('Inicio de sesión exitoso');
      // Redireccionar al catálogo
      window.location.href = './catalog.html';
    } else {
      alert('Correo o contraseña incorrectos');
    }
  });

  // Redirigir al formulario de registro
  registerBtn.addEventListener('click', () => {
    window.location.href = './register.html'; // Asegúrate de tener esta vista creada
  });
});
