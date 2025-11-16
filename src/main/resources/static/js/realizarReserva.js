let reservaSelecionada = null;

document.addEventListener('DOMContentLoaded', function() {
    const dataCheckIn = document.getElementById('dataCheckIn');
    const dataCheckOut = document.getElementById('dataCheckOut');
    const capacidade = document.getElementById('capacidade');

    const hoje = new Date();
    hoje.setDate(hoje.getDate() + 1);
    const amanha = hoje.toISOString().split('T')[0];

    dataCheckIn.setAttribute('min', amanha);

    dataCheckIn.addEventListener('change', function() {
        const checkInDate = new Date(this.value);
        checkInDate.setDate(checkInDate.getDate() + 1);
        const minCheckOut = checkInDate.toISOString().split('T')[0];
        dataCheckOut.setAttribute('min', minCheckOut);
        if (dataCheckOut.value && dataCheckOut.value < minCheckOut) {
            dataCheckOut.value = '';
        }
    });

    const urlParams = new URLSearchParams(window.location.search);
    const checkIn = urlParams.get('checkIn');
    const checkOut = urlParams.get('checkOut');
    const hospedes = urlParams.get('hospedes');

    if (checkIn) {
        dataCheckIn.value = checkIn;
        const checkInDate = new Date(checkIn);
        checkInDate.setDate(checkInDate.getDate() + 1);
        const minCheckOut = checkInDate.toISOString().split('T')[0];
        dataCheckOut.setAttribute('min', minCheckOut);
    }

    if (checkOut) {
        dataCheckOut.value = checkOut;
    }

    if (hospedes) {
        capacidade.value = hospedes;
    }

    if (checkIn && checkOut && hospedes) {
        buscarQuartosDisponiveis();
    }
});

function buscarQuartosDisponiveis() {
    const dataCheckIn = document.getElementById('dataCheckIn').value;
    const dataCheckOut = document.getElementById('dataCheckOut').value;
    const capacidade = document.getElementById('capacidade').value;

    if (!dataCheckIn || !dataCheckOut) {
        alert('Por favor, selecione as datas de check-in e check-out');
        return;
    }

    const checkInDate = new Date(dataCheckIn);
    const checkOutDate = new Date(dataCheckOut);

    if (checkOutDate <= checkInDate) {
        alert('A data de check-out deve ser posterior à data de check-in');
        return;
    }

    const dataCheckInFormatted = dataCheckIn + 'T14:00:00';
    const dataCheckOutFormatted = dataCheckOut + 'T11:00:00';

    fetch('/api/quartos/buscar-disponiveis', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            dataCheckIn: dataCheckInFormatted,
            dataCheckOut: dataCheckOutFormatted,
            capacidadeMinima: parseInt(capacidade)
        })
    })
    .then(response => response.json())
    .then(quartos => {
        exibirQuartos(quartos, dataCheckInFormatted, dataCheckOutFormatted);
    })
    .catch(error => {
        console.error('Erro ao buscar quartos:', error);
        alert('Erro ao buscar quartos disponíveis. Tente novamente.');
    });
}

function exibirQuartos(quartos, dataCheckIn, dataCheckOut) {
    const listaQuartos = document.getElementById('listaQuartos');

    if (quartos.length === 0) {
        listaQuartos.innerHTML = `
            <div class="mensagem-vazio">
                <ion-icon name="alert-circle-outline"></ion-icon>
                <p>Nenhum quarto disponível para as datas e capacidade selecionadas</p>
            </div>
        `;
        return;
    }

    listaQuartos.innerHTML = quartos.map(quarto => {
        const dias = calcularDias(dataCheckIn, dataCheckOut);
        const valorTotal = quarto.valor * dias;

        return `
            <div class="card-quarto">
                <div class="card-imagem">
                    <img src="${quarto.imageUrl}" alt="${quarto.nome}">
                </div>
                <div class="card-info">
                    <div class="card-titulo">
                        <h4>${quarto.nome}</h4>
                        <p>${quarto.descricao}</p>
                    </div>
                    <div class="card-detalhes">
                        <div class="detalhe-item">
                            <ion-icon name="body-outline"></ion-icon>
                            <span>${quarto.capacidadeMax} hóspedes</span>
                        </div>
                        <div class="detalhe-item">
                            <ion-icon name="bed-outline"></ion-icon>
                            <span>${quarto.camas} camas</span>
                        </div>
                    </div>
                    <div class="card-footer">
                        <div class="preco">
                            R$ ${valorTotal.toFixed(2).replace('.', ',')}
                            <span>/ ${dias} diárias</span>
                        </div>
                        <button class="btn-reservar" onclick='selecionarQuarto(${JSON.stringify(quarto)}, "${dataCheckIn}", "${dataCheckOut}", ${valorTotal})'>
                            Reservar
                        </button>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

function calcularDias(dataCheckIn, dataCheckOut) {
    const checkIn = new Date(dataCheckIn);
    const checkOut = new Date(dataCheckOut);
    const diffTime = Math.abs(checkOut - checkIn);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
}

function selecionarQuarto(quarto, dataCheckIn, dataCheckOut, valorTotal) {
    // Verifica se o usuário está logado antes de abrir o modal
    fetch('/api/user/perfil', {
        method: 'GET',
        credentials: 'same-origin'
    })
    .then(response => {
        if (!response.ok) {
            // Usuário não está logado, salva a URL atual e redireciona para login
            const currentUrl = window.location.pathname + window.location.search;
            window.location.href = '/login?redirect=' + encodeURIComponent(currentUrl);
            return;
        }

        // Usuário está logado, prossegue com a reserva
        reservaSelecionada = {
            quartoId: quarto.id,
            quartoNome: quarto.nome,
            dataCheckIn: dataCheckIn,
            dataCheckOut: dataCheckOut,
            valorTotal: valorTotal
        };

        const dias = calcularDias(dataCheckIn, dataCheckOut);
        const checkInFormatted = formatarData(dataCheckIn);
        const checkOutFormatted = formatarData(dataCheckOut);

        document.getElementById('detalhesReserva').innerHTML = `
            <div class="detalhe-linha">
                <span class="label">Quarto:</span>
                <span class="valor">${quarto.nome}</span>
            </div>
            <div class="detalhe-linha">
                <span class="label">Check-in:</span>
                <span class="valor">${checkInFormatted}</span>
            </div>
            <div class="detalhe-linha">
                <span class="label">Check-out:</span>
                <span class="valor">${checkOutFormatted}</span>
            </div>
            <div class="detalhe-linha">
                <span class="label">Diárias:</span>
                <span class="valor">${dias} noites</span>
            </div>
            <div class="detalhe-linha">
                <span class="label">Valor por noite:</span>
                <span class="valor">R$ ${quarto.valor.toFixed(2).replace('.', ',')}</span>
            </div>
            <div class="detalhe-linha total">
                <span class="label">Valor Total:</span>
                <span class="valor">R$ ${valorTotal.toFixed(2).replace('.', ',')}</span>
            </div>
        `;

        abrirModal();
    })
    .catch(error => {
        console.error('Erro ao verificar autenticação:', error);
        // Em caso de erro, redireciona para login por segurança
        const currentUrl = window.location.pathname + window.location.search;
        window.location.href = '/login?redirect=' + encodeURIComponent(currentUrl);
    });
}

function formatarData(dataISO) {
    const data = new Date(dataISO);
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

function abrirModal() {
    const modal = document.getElementById('modalConfirmacao');
    modal.classList.add('active');
}

function fecharModal() {
    const modal = document.getElementById('modalConfirmacao');
    modal.classList.remove('active');
    reservaSelecionada = null;
}

function confirmarReserva() {
    if (!reservaSelecionada) {
        alert('Erro ao processar a reserva. Tente novamente.');
        return;
    }

    fetch('/api/reservas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'same-origin',
        body: JSON.stringify({
            quartoId: reservaSelecionada.quartoId,
            dataCheckIn: reservaSelecionada.dataCheckIn,
            dataCheckOut: reservaSelecionada.dataCheckOut
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao criar reserva');
        }
        return response.json();
    })
    .then(data => {
        alert('Reserva realizada com sucesso!');
        window.location.href = '/reservas';
    })
    .catch(error => {
        console.error('Erro ao confirmar reserva:', error);
        alert('Erro ao confirmar reserva. Verifique se você está logado e tente novamente.');
    });
}

window.onclick = function(event) {
    const modal = document.getElementById('modalConfirmacao');
    if (event.target == modal) {
        fecharModal();
    }
}
