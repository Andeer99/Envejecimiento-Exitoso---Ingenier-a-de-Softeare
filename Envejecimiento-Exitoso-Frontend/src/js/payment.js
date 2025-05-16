document.addEventListener('DOMContentLoaded', () => {
  const totalElement = document.querySelector('.summary-details span');
  const cardDetails = document.querySelector('.card-details');
  const paypalDetails = document.querySelector('.paypal-details');
  const paymentMethodRadios = document.querySelectorAll('input[name="paymentMethod"]');
  const form = document.querySelector('form');

  // Mostrar el total desde localStorage
  const storedTotal = localStorage.getItem('cartTotal');
  if (storedTotal) {
    totalElement.textContent = `$${parseFloat(storedTotal).toFixed(2)}`;
  }

  // Mostrar/ocultar campos según método de pago
  function togglePaymentFields() {
    const selectedMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
    if (selectedMethod === 'card') {
      cardDetails.style.display = 'block';
      paypalDetails.style.display = 'none';
    } else {
      cardDetails.style.display = 'none';
      paypalDetails.style.display = 'block';
    }
  }

  // Inicializar campos al cargar
  togglePaymentFields();

  // Escuchar cambios en el método de pago
  paymentMethodRadios.forEach(radio => {
    radio.addEventListener('change', togglePaymentFields);
  });

  // Validar y simular confirmación de pago
  form.addEventListener('submit', (e) => {
    e.preventDefault();
    const method = document.querySelector('input[name="paymentMethod"]:checked').value;

    if (method === 'card') {
      const cardNumber = document.getElementById('cardNumber').value.trim();
      const expiry = document.getElementById('expiry').value.trim();
      const cvv = document.getElementById('cvv').value.trim();

      if (!cardNumber || !expiry || !cvv) {
        alert('Por favor completa todos los campos de tarjeta.');
        return;
      }
    } else {
      const paypalEmail = document.getElementById('paypalEmail').value.trim();
      if (!paypalEmail) {
        alert('Por favor ingresa tu correo de PayPal.');
        return;
      }
    }

    alert('¡Pago realizado con éxito! Gracias por tu compra.');

    // Limpiar carrito (simulado)
    localStorage.removeItem('cartItems');
    localStorage.removeItem('cartTotal');

    // Redirigir a una pantalla de confirmación si se desea
    window.location.href = 'confirmation.html'; // Opcional
  });
});
