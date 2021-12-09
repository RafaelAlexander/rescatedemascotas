
    create table Administrador (
        id bigint not null,
        primary key (id)
    )

    create table Adopcion (
        DTYPE varchar(31) not null,
        id bigint not null,
        identificador varchar(255),
        mascota_chapita bigint,
        asociacion_id bigint,
        primary key (id)
    )

    create table Adoptante (
        id bigint not null,
        email varchar(255),
        asociacion_id bigint,
        primary key (id)
    )

    create table AnimalPerdido (
        id bigint not null,
        chapita bigint,
        descripcionEstado varchar(255),
        posX double not null,
        posY double not null,
        primary key (id)
    )

    create table Asociacion (
        id bigint not null,
        posicionX double not null,
        posicionY double not null,
        primary key (id)
    )

    create table Asociacion_preguntasAdopcion (
        Asociacion_id bigint not null,
        preguntasAdopcion varchar(255)
    )

    create table Caracteristica (
        id bigint not null,
        descripcion varchar(255),
        tipoCaracteristica varchar(255),
        primary key (id)
    )

    create table Caracteristica_valores (
        Caracteristica_id bigint not null,
        valores varchar(255)
    )

    create table Contacto (
        id bigint not null,
        email varchar(255),
        nombreApellido varchar(255),
        telefono varchar(255),
        dp_nombre varchar(255),
        dp_apellido varchar(255),
        dp_tipoDoc varchar(255),
        dp_nroDoc varchar(255),
        primary key (id)
    )

    create table DatosPersonales (
        apellido varchar(255) not null,
        nombre varchar(255) not null,
        numeroDeDocumento varchar(255) not null,
        tipoDocumento varchar(255) not null,
        direccion varchar(255),
        fechaDeNacimiento date,
        primary key (apellido, nombre, numeroDeDocumento, tipoDocumento)
    )

    create table Detalle (
        id bigint not null,
        pregunta varchar(255),
        respuesta varchar(255),
        primary key (id)
    )

    create table Duenio (
        id bigint not null,
        datosPersonales_apellido varchar(255),
        datosPersonales_nombre varchar(255),
        datosPersonales_numeroDeDocumento varchar(255),
        datosPersonales_tipoDocumento varchar(255),
        primary key (id)
    )

    create table Duenio_notificaciones (
        Duenio_id bigint not null,
        notificaciones varchar(255)
    )

    create table Foto (
        id bigint not null,
        data blob(255),
        animalPerdido_id bigint,
        mascota_id bigint,
        primary key (id)
    )

    create table Mascota (
        chapita bigint not null,
        apodo varchar(255),
        descripcionFisica varchar(255),
        edadAproximada integer not null,
        nombre varchar(255),
        tipoMascota integer,
        tipoSexo integer,
        duenio_id bigint,
        primary key (chapita)
    )

    create table Mascota_Caracteristica (
        Mascota_chapita bigint not null,
        caracteristicas_id bigint not null
    )

    create table ParticipanteRescate (
        id bigint not null,
        datosPersonales_apellido varchar(255),
        datosPersonales_nombre varchar(255),
        datosPersonales_numeroDeDocumento varchar(255),
        datosPersonales_tipoDocumento varchar(255),
        primary key (id)
    )

    create table PublicacionAprobada (
        id bigint not null,
        animalPerdido_id bigint,
        duenioReclamante_id bigint,
        rescatista_id bigint,
        asociacion_id bigint,
        primary key (id)
    )

    create table Rescate (
        id bigint not null,
        fecha date,
        animalPerdido_id bigint,
        rescatista_id bigint,
        primary key (id)
    )

    create table RescateSinChapita (
        id bigint not null,
        animalPerdido_id bigint,
        rescatista_id bigint,
        primary key (id)
    )

    create table Usuario (
        id bigint not null,
        password varchar(255),
        username varchar(255),
        cuentaAdministrador_id bigint,
        cuentaDeDuenio_id bigint,
        primary key (id)
    )

    create table Voluntario (
        id bigint not null,
        asociacion_id bigint,
        usuario_id bigint,
        primary key (id)
    )

    create table adoptante_id (
        Adoptante_id bigint not null,
        detalleList_id bigint not null
    )

    create table contacto_id (
        Adopcion_id bigint not null,
        detalles_id bigint not null,
        contactos_id bigint not null
    )

    alter table adoptante_id 
        add constraint UK_ptbx4v2t3j1wbkdanlvkcklp0  unique (detalleList_id)

    alter table contacto_id 
        add constraint UK_b7hpvfnauypnimbwtykgappw3  unique (detalles_id)

    alter table contacto_id 
        add constraint UK_8lvrj4tun55usty5wn1ds6dyq  unique (contactos_id)

    alter table Adopcion 
        add constraint FK_9j7gin05iu2jo6ylgh33puqct 
        foreign key (mascota_chapita) 
        references Mascota

    alter table Adopcion 
        add constraint FK_299toc02r7rwr00nxycv2kj23 
        foreign key (asociacion_id) 
        references Asociacion

    alter table Adoptante 
        add constraint FK_8ljt4gk6tc26yt4a2m35y56qw 
        foreign key (asociacion_id) 
        references Asociacion

    alter table Asociacion_preguntasAdopcion 
        add constraint FK_dwxj01c20cd6yf0vwjtupc2v6 
        foreign key (Asociacion_id) 
        references Asociacion

    alter table Caracteristica_valores 
        add constraint FK_8yumusmdrchtqje5n8nrte3ga 
        foreign key (Caracteristica_id) 
        references Caracteristica

    alter table Contacto 
        add constraint FK_hbwiir9n6so73h9bm5b0p3lb5 
        foreign key (dp_nombre, dp_apellido, dp_tipoDoc, dp_nroDoc) 
        references DatosPersonales

    alter table Duenio 
        add constraint FK_nmtp7jptaq4yis9r1x7f6j0oy 
        foreign key (datosPersonales_apellido, datosPersonales_nombre, datosPersonales_numeroDeDocumento, datosPersonales_tipoDocumento) 
        references DatosPersonales

    alter table Duenio_notificaciones 
        add constraint FK_2oe7rafjunm1p75evw0fgwxgv 
        foreign key (Duenio_id) 
        references Duenio

    alter table Foto 
        add constraint FK_k31vwpbmgrbwlh35tl2s7y485 
        foreign key (animalPerdido_id) 
        references AnimalPerdido

    alter table Foto 
        add constraint FK_ar66uq0yvvmf9pq0l6j1os1nx 
        foreign key (mascota_id) 
        references Mascota

    alter table Mascota 
        add constraint FK_jp5ybetmdo7q1bcwaa1p1mwjs 
        foreign key (duenio_id) 
        references Duenio

    alter table Mascota_Caracteristica 
        add constraint FK_3v6pmbo9vuwp6rtb1u8mj2adg 
        foreign key (caracteristicas_id) 
        references Caracteristica

    alter table Mascota_Caracteristica 
        add constraint FK_g1v9htsev1a3awprq85ihr7kv 
        foreign key (Mascota_chapita) 
        references Mascota

    alter table ParticipanteRescate 
        add constraint FK_64jrb7rs2o2c3ao1s0a7w5yjs 
        foreign key (datosPersonales_apellido, datosPersonales_nombre, datosPersonales_numeroDeDocumento, datosPersonales_tipoDocumento) 
        references DatosPersonales

    alter table PublicacionAprobada 
        add constraint FK_65sp2mwyng2jylca8jk32vlwk 
        foreign key (animalPerdido_id) 
        references AnimalPerdido

    alter table PublicacionAprobada 
        add constraint FK_d0k6udr6u8s5qy5kogsbbj3ph 
        foreign key (duenioReclamante_id) 
        references ParticipanteRescate

    alter table PublicacionAprobada 
        add constraint FK_cy7twqb20x6sm08yxl5bg8eyk 
        foreign key (rescatista_id) 
        references ParticipanteRescate

    alter table PublicacionAprobada 
        add constraint FK_ieg7cq5fcp6hob0juiefmmhxs 
        foreign key (asociacion_id) 
        references Asociacion

    alter table Rescate 
        add constraint FK_ns9i4e4vql4iwvor2qfu3ckuu 
        foreign key (animalPerdido_id) 
        references AnimalPerdido

    alter table Rescate 
        add constraint FK_ago1xivxj42egptbb0ugkiap 
        foreign key (rescatista_id) 
        references ParticipanteRescate

    alter table RescateSinChapita 
        add constraint FK_ff63hydtq69y9yunk4gpm7an7 
        foreign key (animalPerdido_id) 
        references AnimalPerdido

    alter table RescateSinChapita 
        add constraint FK_fd8rp02ya8o6fmx5b7wbs9dp3 
        foreign key (rescatista_id) 
        references ParticipanteRescate

    alter table Usuario 
        add constraint FK_q1659vdo8q940tii2mykj87sx 
        foreign key (cuentaAdministrador_id) 
        references Administrador

    alter table Usuario 
        add constraint FK_7qntawuxp1wgyhaxqr4vd8h25 
        foreign key (cuentaDeDuenio_id) 
        references Duenio

    alter table Voluntario 
        add constraint FK_15jg7c6d6l9uuqpibrvatuw9d 
        foreign key (asociacion_id) 
        references Asociacion

    alter table Voluntario 
        add constraint FK_h17g1gm6lm00nee069iw3wswl 
        foreign key (usuario_id) 
        references Usuario

    alter table adoptante_id 
        add constraint FK_ptbx4v2t3j1wbkdanlvkcklp0 
        foreign key (detalleList_id) 
        references Detalle

    alter table adoptante_id 
        add constraint FK_7gnvyg3gg2dy0tckraco7cy27 
        foreign key (Adoptante_id) 
        references Adoptante

    alter table contacto_id 
        add constraint FK_b7hpvfnauypnimbwtykgappw3 
        foreign key (detalles_id) 
        references Detalle

    alter table contacto_id 
        add constraint FK_9tled5a16eosc9u3rw9sb3cv0 
        foreign key (Adopcion_id) 
        references Adopcion

    alter table contacto_id 
        add constraint FK_8lvrj4tun55usty5wn1ds6dyq 
        foreign key (contactos_id) 
        references Contacto

    create table hibernate_sequences (
         sequence_name varchar(255),
         sequence_next_hi_value integer 
    )
