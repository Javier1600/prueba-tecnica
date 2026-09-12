-- =====================================================================
-- BASE DE DATOS 1: clientes
-- =====================================================================

CREATE DATABASE clientes;
\c clientes;


-- ---------------------------------------------------------------------
-- Tabla: persona
-- ---------------------------------------------------------------------
CREATE TABLE persona (
    persona_id      BIGSERIAL       PRIMARY KEY,
    nombre          VARCHAR(100)    NOT NULL,
    genero          VARCHAR(20),
    edad            INTEGER,
    identificacion  VARCHAR(20)     NOT NULL,
    direccion       VARCHAR(200),
    telefono        VARCHAR(20)
);

COMMENT ON TABLE persona IS 'Tabla de personas';

-- ---------------------------------------------------------------------
-- Tabla: cliente (hereda de persona, estrategia JOINED)
-- ---------------------------------------------------------------------
CREATE TABLE cliente (
    cliente_id      BIGINT          PRIMARY KEY,
    estado          BOOLEAN         NOT NULL DEFAULT TRUE,
    password        VARCHAR(100),
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (cliente_id) REFERENCES persona(persona_id)
        ON DELETE CASCADE
);

COMMENT ON TABLE cliente IS 'Tabla de clientes';

-- =====================================================================
-- BASE DE DATOS 2: cuentas
-- =====================================================================

CREATE DATABASE cuentas;
\c cuentas;

-- ---------------------------------------------------------------------
-- Tabla: cliente
-- ---------------------------------------------------------------------
CREATE TABLE cliente (
    cliente_id              BIGINT          PRIMARY KEY,
    nombre                  VARCHAR(100)    NOT NULL,
    identificacion          VARCHAR(20),
    estado                  BOOLEAN,
    fecha_sincronizacion    TIMESTAMP
);

COMMENT ON TABLE cliente IS 'Proyección local de clientes';


-- ---------------------------------------------------------------------
-- Tabla: cuenta
-- ---------------------------------------------------------------------
CREATE TABLE cuenta (
    cuenta_id           BIGSERIAL       PRIMARY KEY,
    numero_cuenta       VARCHAR(20)     NOT NULL UNIQUE,
    tipo_cuenta         VARCHAR(20)     NOT NULL,
    saldo_inicial       NUMERIC(15,2)   NOT NULL,
    saldo_disponible    NUMERIC(15,2)   NOT NULL,
    estado              BOOLEAN         NOT NULL DEFAULT TRUE,
    cliente_id          BIGINT          NOT NULL,
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id)
);

CREATE INDEX idx_cuenta_cliente ON cuenta(cliente_id);
CREATE INDEX idx_cuenta_estado ON cuenta(estado);

COMMENT ON TABLE cuenta IS 'Tabla de cuentas bancarias';


CREATE TABLE movimiento (
    movimiento_id       BIGSERIAL       PRIMARY KEY,
    fecha               TIMESTAMP       NOT NULL,
    tipo_movimiento     VARCHAR(20)     NOT NULL,
    valor               NUMERIC(15,2)   NOT NULL,
    saldo               NUMERIC(15,2)   NOT NULL,
    cuenta_id           BIGINT          NOT NULL,
    estado              BOOLEAN         NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES cuenta(cuenta_id)
);

CREATE INDEX idx_movimiento_cuenta ON movimiento(cuenta_id);
CREATE INDEX idx_movimiento_fecha ON movimiento(fecha);

COMMENT ON TABLE movimiento IS 'Tabla de movimientos en cuentas';
