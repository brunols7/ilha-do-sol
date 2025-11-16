// === CAROUSEL PRINCIPAL ===
const carousel = document.getElementById("carousel");
const prev = document.querySelector(".prev");
const next = document.querySelector(".next");

function getStepMain() {
  const firstCard = carousel.querySelector(".card");
  if (!firstCard) return 0;
  const style = window.getComputedStyle(firstCard);
  const margin = parseFloat(style.marginLeft) + parseFloat(style.marginRight);
  return firstCard.getBoundingClientRect().width + margin;
}

function scrollToNextMain(dir = 1) {
  const step = getStepMain();
  carousel.scrollBy({ left: dir * step, behavior: "smooth" });
}

next.addEventListener("click", () => scrollToNextMain(1));
prev.addEventListener("click", () => scrollToNextMain(-1));

// (opcional) setas do teclado
window.addEventListener("keydown", (e) => {
  if (e.key === "ArrowRight") scrollToNextMain(1);
  if (e.key === "ArrowLeft") scrollToNextMain(-1);
});

// === CAROUSEL DE DEPOIMENTOS ===
const depoCarousel = document.getElementById("carousel-depoimentos");
const depoPrev = document.querySelector(".prev-next");
const depoNext = document.querySelector(".next-prev");

function getStepDepo() {
  const firstCard = depoCarousel.querySelector(".depo");
  if (!firstCard) return 0;
  const style = window.getComputedStyle(firstCard);
  const margin = parseFloat(style.marginLeft) + parseFloat(style.marginRight);
  return firstCard.getBoundingClientRect().width + margin;
}

function scrollToNextDepo(dir = 1) {
  const step = getStepDepo();
  depoCarousel.scrollBy({ left: dir * step, behavior: "smooth" });
}

depoNext.addEventListener("click", () => scrollToNextDepo(1));
depoPrev.addEventListener("click", () => scrollToNextDepo(-1));

window.addEventListener("keydown", (e) => {
  if (e.key === "d") scrollToNextDepo(1);
  if (e.key === "a") scrollToNextDepo(-1);
});

document.addEventListener('DOMContentLoaded', function() {
  const dataEntrada = document.getElementById('dataEntradaHome');
  const dataSaida = document.getElementById('dataSaidaHome');

  const hoje = new Date();
  hoje.setDate(hoje.getDate() + 1);
  const amanha = hoje.toISOString().split('T')[0];

  dataEntrada.setAttribute('min', amanha);

  dataEntrada.addEventListener('change', function() {
    const entradaDate = new Date(this.value);
    entradaDate.setDate(entradaDate.getDate() + 1);
    const minSaida = entradaDate.toISOString().split('T')[0];
    dataSaida.setAttribute('min', minSaida);
    dataSaida.value = '';
  });
});

function buscarReservaHome() {
  const dataEntrada = document.getElementById('dataEntradaHome').value;
  const dataSaida = document.getElementById('dataSaidaHome').value;
  const hospedes = document.getElementById('hospedesHome').value;

  if (!dataEntrada || !dataSaida) {
    alert('Por favor, selecione as datas de entrada e saída');
    return;
  }

  const url = `/reservar?checkIn=${dataEntrada}&checkOut=${dataSaida}&hospedes=${hospedes}`;
  window.location.href = url;
}
