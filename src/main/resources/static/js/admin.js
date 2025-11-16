let todasReservas = [];

document.addEventListener('DOMContentLoaded', function() {
    carregarQuartos();
    carregarReservas();
});

function openTab(tabName) {
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => button.classList.remove('active'));

    document.getElementById(tabName).classList.add('active');
    event.target.closest('.tab-button').classList.add('active');
}

function carregarQuartos() {
    fetch('/api/quartos')
        .then(response => response.json())
        .then(quartos => {
            exibirQuartos(quartos);
        })
        .catch(error => {
            console.error('Erro ao carregar quartos:', error);
            document.getElementById('tabelaQuartos').innerHTML = `
                <tr><td colspan="7" class="loading">Erro ao carregar quartos</td></tr>
            `;
        });
}

function exibirQuartos(quartos) {
    const tbody = document.getElementById('tabelaQuartos');

    if (quartos.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="7" class="loading">Nenhum quarto cadastrado</td></tr>
        `;
        return;
    }

    tbody.innerHTML = quartos.map(quarto => `
        <tr>
            <td>${quarto.id}</td>
            <td>${quarto.nome}</td>
            <td>${quarto.numeroQuarto}</td>
            <td>${quarto.capacidadeMax}</td>
            <td>${quarto.camas}</td>
            <td>R$ ${quarto.valor.toFixed(2).replace('.', ',')}</td>
            <td>
                <div class="acoes">
                    <button class="btn-editar" onclick='editarQuarto(${JSON.stringify(quarto)})'>
                        <ion-icon name="create-outline"></ion-icon>
                        Editar
                    </button>
                    <button class="btn-excluir" onclick="excluirQuarto(${quarto.id})">
                        <ion-icon name="trash-outline"></ion-icon>
                        Excluir
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

function abrirModalNovoQuarto() {
    document.getElementById('tituloModalQuarto').textContent = 'Novo Quarto';
    document.getElementById('formQuarto').reset();
    document.getElementById('quartoId').value = '';
    abrirModalQuarto();
}

function editarQuarto(quarto) {
    document.getElementById('tituloModalQuarto').textContent = 'Editar Quarto';
    document.getElementById('quartoId').value = quarto.id;
    document.getElementById('quartoNome').value = quarto.nome;
    document.getElementById('quartoDescricao').value = quarto.descricao;
    document.getElementById('quartoNumero').value = quarto.numeroQuarto;
    document.getElementById('quartoCapacidade').value = quarto.capacidadeMax;
    document.getElementById('quartoCamas').value = quarto.camas;
    document.getElementById('quartoValor').value = quarto.valor;
    document.getElementById('quartoImagem').value = quarto.imageUrl || '';
    abrirModalQuarto();
}

function salvarQuarto(event) {
    event.preventDefault();

    const quartoId = document.getElementById('quartoId').value;
    const quarto = {
        nome: document.getElementById('quartoNome').value,
        descricao: document.getElementById('quartoDescricao').value,
        numeroQuarto: document.getElementById('quartoNumero').value,
        capacidadeMax: parseInt(document.getElementById('quartoCapacidade').value),
        camas: parseInt(document.getElementById('quartoCamas').value),
        valor: parseFloat(document.getElementById('quartoValor').value),
        imageUrl: document.getElementById('quartoImagem').value || '/img/quarto-1.jpg'
    };

    const url = quartoId ? `/api/quartos/${quartoId}` : '/api/quartos';
    const method = quartoId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(quarto)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao salvar quarto');
        }
        return response.json();
    })
    .then(() => {
        alert(quartoId ? 'Quarto atualizado com sucesso!' : 'Quarto criado com sucesso!');
        fecharModalQuarto();
        carregarQuartos();
    })
    .catch(error => {
        console.error('Erro ao salvar quarto:', error);
        alert('Erro ao salvar quarto. Verifique os dados e tente novamente.');
    });
}

function excluirQuarto(id) {
    if (!confirm('Tem certeza que deseja excluir este quarto?')) {
        return;
    }

    fetch(`/api/quartos/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao excluir quarto');
        }
        alert('Quarto excluído com sucesso!');
        carregarQuartos();
    })
    .catch(error => {
        console.error('Erro ao excluir quarto:', error);
        alert('Erro ao excluir quarto. Pode haver reservas vinculadas a este quarto.');
    });
}

function abrirModalQuarto() {
    document.getElementById('modalQuarto').classList.add('active');
}

function fecharModalQuarto() {
    document.getElementById('modalQuarto').classList.remove('active');
}

function carregarReservas() {
    fetch('/api/reservas', {
        credentials: 'same-origin'
    })
        .then(response => response.json())
        .then(reservas => {
            todasReservas = reservas;
            exibirReservas(reservas);
        })
        .catch(error => {
            console.error('Erro ao carregar reservas:', error);
            document.getElementById('tabelaReservas').innerHTML = `
                <tr><td colspan="7" class="loading">Erro ao carregar reservas</td></tr>
            `;
        });
}

function exibirReservas(reservas) {
    const tbody = document.getElementById('tabelaReservas');

    if (reservas.length === 0) {
        tbody.innerHTML = `
            <tr><td colspan="7" class="loading">Nenhuma reserva encontrada</td></tr>
        `;
        return;
    }

    tbody.innerHTML = reservas.map(reserva => {
        const statusClass = reserva.status.toLowerCase();
        const checkIn = formatarData(reserva.dataCheckIn);
        const checkOut = formatarData(reserva.dataCheckOut);

        return `
            <tr>
                <td>${reserva.id}</td>
                <td>${reserva.user.username}</td>
                <td>${reserva.quarto.nome}</td>
                <td>${checkIn}</td>
                <td>${checkOut}</td>
                <td>
                    <span class="status-badge ${statusClass}">${reserva.status}</span>
                </td>
                <td>
                    <div class="acoes">
                        <button class="btn-status" onclick="abrirModalAlterarStatus(${reserva.id}, '${reserva.status}')">
                            <ion-icon name="swap-horizontal-outline"></ion-icon>
                            Status
                        </button>
                        <button class="btn-excluir" onclick="excluirReserva(${reserva.id})">
                            <ion-icon name="trash-outline"></ion-icon>
                            Excluir
                        </button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');
}

function formatarData(dataISO) {
    const data = new Date(dataISO);
    const dia = String(data.getDate()).padStart(2, '0');
    const mes = String(data.getMonth() + 1).padStart(2, '0');
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

function filtrarReservas() {
    const filtroStatus = document.getElementById('filtroStatus').value;

    if (!filtroStatus) {
        exibirReservas(todasReservas);
        return;
    }

    const reservasFiltradas = todasReservas.filter(r => r.status === filtroStatus);
    exibirReservas(reservasFiltradas);
}

function abrirModalAlterarStatus(reservaId, statusAtual) {
    document.getElementById('reservaId').value = reservaId;
    document.getElementById('reservaStatus').value = statusAtual;
    abrirModalReserva();
}

function alterarStatusReserva(event) {
    event.preventDefault();

    const reservaId = document.getElementById('reservaId').value;
    const novoStatus = document.getElementById('reservaStatus').value;

    fetch(`/api/reservas/${reservaId}/status`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'same-origin',
        body: JSON.stringify({ status: novoStatus })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao alterar status');
        }
        return response.json();
    })
    .then(() => {
        alert('Status alterado com sucesso!');
        fecharModalReserva();
        carregarReservas();
    })
    .catch(error => {
        console.error('Erro ao alterar status:', error);
        alert('Erro ao alterar status da reserva.');
    });
}

function excluirReserva(id) {
    if (!confirm('Tem certeza que deseja excluir esta reserva?')) {
        return;
    }

    fetch(`/api/reservas/${id}`, {
        method: 'DELETE',
        credentials: 'same-origin'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro ao excluir reserva');
        }
        alert('Reserva excluída com sucesso!');
        carregarReservas();
    })
    .catch(error => {
        console.error('Erro ao excluir reserva:', error);
        alert('Erro ao excluir reserva.');
    });
}

function abrirModalReserva() {
    document.getElementById('modalReserva').classList.add('active');
}

function fecharModalReserva() {
    document.getElementById('modalReserva').classList.remove('active');
}

window.onclick = function(event) {
    const modalQuarto = document.getElementById('modalQuarto');
    const modalReserva = document.getElementById('modalReserva');

    if (event.target == modalQuarto) {
        fecharModalQuarto();
    }
    if (event.target == modalReserva) {
        fecharModalReserva();
    }
}
