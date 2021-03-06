PGDMP         6                y            tripapp    14.1    14.1 0    0           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            1           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            2           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            3           1262    16478    tripapp    DATABASE     f   CREATE DATABASE tripapp WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'Albanian_Albania.1250';
    DROP DATABASE tripapp;
                postgres    false            Y           1247    16565    flight_package_ctype    TYPE     ?   CREATE TYPE public.flight_package_ctype AS (
	flights bigint[],
	departure_date timestamp without time zone,
	arrival_date timestamp without time zone,
	stepovers character varying[]
);
 '   DROP TYPE public.flight_package_ctype;
       public          postgres    false            ?            1259    16520    booking_sequence    SEQUENCE     y   CREATE SEQUENCE public.booking_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.booking_sequence;
       public          postgres    false            ?            1259    16479    bookings    TABLE     c   CREATE TABLE public.bookings (
    id bigint NOT NULL,
    flight_id bigint,
    trip_id bigint
);
    DROP TABLE public.bookings;
       public         heap    postgres    false            ?            1259    16521    course_sequence    SEQUENCE     y   CREATE SEQUENCE public.course_sequence
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.course_sequence;
       public          postgres    false            ?            1259    16522    flight_sequence    SEQUENCE     x   CREATE SEQUENCE public.flight_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.flight_sequence;
       public          postgres    false            ?            1259    16484    flights    TABLE     )  CREATE TABLE public.flights (
    id bigint NOT NULL,
    arrival_date timestamp without time zone NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    from_city character varying(255) NOT NULL,
    to_city character varying(255) NOT NULL,
    price double precision NOT NULL
);
    DROP TABLE public.flights;
       public         heap    postgres    false            ?            1259    16523    refreshtoken_sequence    SEQUENCE     ~   CREATE SEQUENCE public.refreshtoken_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.refreshtoken_sequence;
       public          postgres    false            ?            1259    16491    refreshtokens    TABLE     ?   CREATE TABLE public.refreshtokens (
    id bigint NOT NULL,
    expiration timestamp without time zone NOT NULL,
    token character varying(255) NOT NULL,
    user_id bigint
);
 !   DROP TABLE public.refreshtokens;
       public         heap    postgres    false            ?            1259    16524    role_sequence    SEQUENCE     v   CREATE SEQUENCE public.role_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.role_sequence;
       public          postgres    false            ?            1259    16496    roles    TABLE     V   CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(25)
);
    DROP TABLE public.roles;
       public         heap    postgres    false            ?            1259    16525    trip_sequence    SEQUENCE     v   CREATE SEQUENCE public.trip_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.trip_sequence;
       public          postgres    false            ?            1259    16501    trips    TABLE     ?  CREATE TABLE public.trips (
    id bigint NOT NULL,
    arrival_date timestamp without time zone NOT NULL,
    departure_date timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    from_city character varying(255) NOT NULL,
    reason character varying(15) NOT NULL,
    status character varying(25),
    to_city character varying(255) NOT NULL,
    user_id bigint
);
    DROP TABLE public.trips;
       public         heap    postgres    false            ?            1259    16508 
   user_roles    TABLE     ]   CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);
    DROP TABLE public.user_roles;
       public         heap    postgres    false            ?            1259    16567    user_sequence    SEQUENCE     v   CREATE SEQUENCE public.user_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.user_sequence;
       public          postgres    false            ?            1259    16511    users    TABLE     ?   CREATE TABLE public.users (
    id bigint NOT NULL,
    password character varying(128) NOT NULL,
    username character varying(20) NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false                       0    16479    bookings 
   TABLE DATA           :   COPY public.bookings (id, flight_id, trip_id) FROM stdin;
    public          postgres    false    209   ?5       !          0    16484    flights 
   TABLE DATA           ^   COPY public.flights (id, arrival_date, departure_date, from_city, to_city, price) FROM stdin;
    public          postgres    false    210   	6       "          0    16491    refreshtokens 
   TABLE DATA           G   COPY public.refreshtokens (id, expiration, token, user_id) FROM stdin;
    public          postgres    false    211   ?6       #          0    16496    roles 
   TABLE DATA           )   COPY public.roles (id, name) FROM stdin;
    public          postgres    false    212   I9       $          0    16501    trips 
   TABLE DATA           {   COPY public.trips (id, arrival_date, departure_date, description, from_city, reason, status, to_city, user_id) FROM stdin;
    public          postgres    false    213   z9       %          0    16508 
   user_roles 
   TABLE DATA           6   COPY public.user_roles (user_id, role_id) FROM stdin;
    public          postgres    false    214   ":       &          0    16511    users 
   TABLE DATA           7   COPY public.users (id, password, username) FROM stdin;
    public          postgres    false    215   Y:       4           0    0    booking_sequence    SEQUENCE SET     >   SELECT pg_catalog.setval('public.booking_sequence', 2, true);
          public          postgres    false    216            5           0    0    course_sequence    SEQUENCE SET     >   SELECT pg_catalog.setval('public.course_sequence', 1, false);
          public          postgres    false    217            6           0    0    flight_sequence    SEQUENCE SET     >   SELECT pg_catalog.setval('public.flight_sequence', 14, true);
          public          postgres    false    218            7           0    0    refreshtoken_sequence    SEQUENCE SET     D   SELECT pg_catalog.setval('public.refreshtoken_sequence', 22, true);
          public          postgres    false    219            8           0    0    role_sequence    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.role_sequence', 3, true);
          public          postgres    false    220            9           0    0    trip_sequence    SEQUENCE SET     <   SELECT pg_catalog.setval('public.trip_sequence', 14, true);
          public          postgres    false    221            :           0    0    user_sequence    SEQUENCE SET     <   SELECT pg_catalog.setval('public.user_sequence', 18, true);
          public          postgres    false    223                       2606    16483    bookings bookings_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.bookings DROP CONSTRAINT bookings_pkey;
       public            postgres    false    209            ?           2606    16490    flights flights_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.flights
    ADD CONSTRAINT flights_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.flights DROP CONSTRAINT flights_pkey;
       public            postgres    false    210            ?           2606    16495     refreshtokens refreshtokens_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.refreshtokens
    ADD CONSTRAINT refreshtokens_pkey PRIMARY KEY (id);
 J   ALTER TABLE ONLY public.refreshtokens DROP CONSTRAINT refreshtokens_pkey;
       public            postgres    false    211            ?           2606    16500    roles roles_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.roles DROP CONSTRAINT roles_pkey;
       public            postgres    false    212            ?           2606    16507    trips trips_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.trips
    ADD CONSTRAINT trips_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.trips DROP CONSTRAINT trips_pkey;
       public            postgres    false    213            ?           2606    16517 *   refreshtokens uk_1lfxk1odre7ch1v66hsfi5xq6 
   CONSTRAINT     f   ALTER TABLE ONLY public.refreshtokens
    ADD CONSTRAINT uk_1lfxk1odre7ch1v66hsfi5xq6 UNIQUE (token);
 T   ALTER TABLE ONLY public.refreshtokens DROP CONSTRAINT uk_1lfxk1odre7ch1v66hsfi5xq6;
       public            postgres    false    211            ?           2606    16519 !   users ukr43af9ap4edm43mmtq01oddj6 
   CONSTRAINT     `   ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);
 K   ALTER TABLE ONLY public.users DROP CONSTRAINT ukr43af9ap4edm43mmtq01oddj6;
       public            postgres    false    215            ?           2606    16515    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    215            ?           1259    16566    idx_trips_user_id    INDEX     F   CREATE INDEX idx_trips_user_id ON public.trips USING btree (user_id);
 %   DROP INDEX public.idx_trips_user_id;
       public            postgres    false    213            ?           2606    16531 $   bookings fk76g5jpvf8bcqejvp5d2vgrnjb    FK CONSTRAINT     ?   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fk76g5jpvf8bcqejvp5d2vgrnjb FOREIGN KEY (trip_id) REFERENCES public.trips(id);
 N   ALTER TABLE ONLY public.bookings DROP CONSTRAINT fk76g5jpvf8bcqejvp5d2vgrnjb;
       public          postgres    false    213    209    3210            ?           2606    16541 !   trips fk8wb14dx6ed0bpp3planbay88u    FK CONSTRAINT     ?   ALTER TABLE ONLY public.trips
    ADD CONSTRAINT fk8wb14dx6ed0bpp3planbay88u FOREIGN KEY (user_id) REFERENCES public.users(id);
 K   ALTER TABLE ONLY public.trips DROP CONSTRAINT fk8wb14dx6ed0bpp3planbay88u;
       public          postgres    false    3214    213    215            ?           2606    16536 )   refreshtokens fkg71xhi5ujnqbgw2rcvtxyrc8s    FK CONSTRAINT     ?   ALTER TABLE ONLY public.refreshtokens
    ADD CONSTRAINT fkg71xhi5ujnqbgw2rcvtxyrc8s FOREIGN KEY (user_id) REFERENCES public.users(id);
 S   ALTER TABLE ONLY public.refreshtokens DROP CONSTRAINT fkg71xhi5ujnqbgw2rcvtxyrc8s;
       public          postgres    false    211    215    3214            ?           2606    16546 &   user_roles fkh8ciramu9cc9q3qcqiv4ue8a6    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);
 P   ALTER TABLE ONLY public.user_roles DROP CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6;
       public          postgres    false    214    3207    212            ?           2606    16551 &   user_roles fkhfh9dx7w3ubf1co1vdev94g3f    FK CONSTRAINT     ?   ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);
 P   ALTER TABLE ONLY public.user_roles DROP CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f;
       public          postgres    false    3214    214    215            ?           2606    16526 $   bookings fkidcytqkgq0ve4x1elcnbmdy8a    FK CONSTRAINT     ?   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT fkidcytqkgq0ve4x1elcnbmdy8a FOREIGN KEY (flight_id) REFERENCES public.flights(id);
 N   ALTER TABLE ONLY public.bookings DROP CONSTRAINT fkidcytqkgq0ve4x1elcnbmdy8a;
       public          postgres    false    210    209    3201                   x?3?4?44?2?4Q1z\\\ ?w      !   ?   x???K?0D??\ ?q ?*??R?M?]???>	??X??<?!R?T????޺?Y5T?ʠ????(??Pt?=$?Gh??DM[&?H??.ʝ?'?l=sRaq??¹:pM2c??^?%a`??j??????~?e???˺?????23?dDDԦ>B??iD???????g?????6?M!ق {4?#%?>??O??5?T??H??E?4N??˻&?5??_      "   >  x?]?ɍ?HE?J+?
??![????7a???T^?F?? ?!z???8??G?mJ0>??0T6???Ό?6]??????<?7I?5?F?~?{HD?.S`g?iB?P>????-???%?!X?8???i?8??]N^?l?(????&klr???Lo-??4^??H?3=??/??@??a?B?P??-i?`4?2u??v?s?~???(?l+Ot?9??p?????˄A٠o?g?$????D??~???,ۘm???6Γl?/~??7D??7?
ϯ???V?E?A?"t???笪Ʋ?h???h?=?wsy??,Np?
?2????NrvPeRJ??!?]"??n$-???+t???u?9FQ???M???bX{a??ke?????sw????Ѣ?(??!?t????ْmf??? <b??k?	:????R?????<??4??Cx˱#fE?,???;??/$n6u??????g?_?%d?_g????аw(?_??c???j??";?1?t--a???p??ZfO?!??????Z?c|?????{?l??Dd?ZJj?-C????,??      #   !   x?3???q?wt????2?pB?]??b???? s??      $   ?   x?}α
?0????y???%P?Vb??P??K?6P???1?
?p??Ǉ
H	??8?1_i(??ʭ??
sOaI?????c??un8??????<?$?(????e???1??Ú???W??v?Vݝ?'??y
\?W?}?3[?{?{q?;?      %   '   x?3?4?24?4?24?LA,sa",@b@V? ???      &   R  x?5?Kr?0  е??5B",)? J K?N7???*?F???w?S.??-gs???b>t?(?R?}lZ??y[<<.?@[??KZ>B?!h?o(?"=???=??R֤??4?PU??y9RVvp?Y?vv??1zz?\???(6????lo?IoLhb?!X??_??2?{"?r???գ?(,?(DDv/???vU??x?????????7?QqT???,b???y)Z??j???1Iw-{<g\?gO?x???B????{??{#_?}sC2??>??7?L?ΊO??u?S Ѐ=1?F?{????t<a???
??tCYp뭬???uP???$?????ڇ?i?/rm??     