INSERT INTO users (username, email, cpf, senha, role, created_at) VALUES
('Admin', 'admin@gmail.com', '12345678900', '$2a$10$cmsnvR0hlZIdVurBu6htIeJkpYh3gl1tpLFllDWJUHkKRI.32MY7i', 'ADMIN', CURRENT_TIMESTAMP);

INSERT INTO hotel_info (nome, telefone, email, logradouro, bairro, cidade, uf, numero, cep) VALUES
('Hotel Ilha do Sol', '(11) 5682-7300', 'contato@ilhadosol.com', 'Av. Eng. Eusébio Stevaux', 'Santo Amaro', 'São Paulo', 'SP', '823', '04696-000');

INSERT INTO quartos (nome, descricao, numero_quarto, capacidade_max, camas, valor, image_url) VALUES
('Suíte Master', 'Suíte espaçosa com vista para o mar, banheira de hidromassagem, varanda privativa e decoração premium', '101', 4, 2, 450.00, 'https://images.unsplash.com/photo-1512918728675-ed5a9ecdebfd?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('Quarto Standard', 'Quarto confortível e aconchegante, ideal para casais, com ar-condicionado e TV a cabo', '202', 2, 1, 180.00, 'https://images.unsplash.com/photo-1618773928121-c32242e63f39?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'),
('Quarto Família', 'Quarto amplo perfeito para famílias, com camas extras e espaço para crianças', '305', 5, 3, 320.00, 'https://images.unsplash.com/photo-1648383228240-6ed939727ad6?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');
