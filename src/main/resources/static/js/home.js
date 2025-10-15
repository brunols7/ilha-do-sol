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

// (opcional) setas do teclado só para depoimentos
window.addEventListener("keydown", (e) => {
  if (e.key === "d") scrollToNextDepo(1); // seta com "d" (direita) só de exemplo
  if (e.key === "a") scrollToNextDepo(-1); // seta com "a" (esquerda)
});
