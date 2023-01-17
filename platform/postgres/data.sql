insert into organization (organization_id, name, contact_name, contact_email, contact_phone) values ('e1103755-87bc-430e-b1a2-61a1b9613708', 'Mitchell and Sons', 'Christiane Debrick', 'cdebrick0@wikimedia.org', '485-446-3456');
insert into organization (organization_id, name, contact_name, contact_email, contact_phone) values ('135635c3-1d91-49f2-a546-14a635972d13', 'Terry, Braun and Will', 'Godard Local', 'glocal1@nps.gov', '505-686-0080');
insert into organization (organization_id, name, contact_name, contact_email, contact_phone) values ('944e6866-ddfc-4e3b-99ac-4fa248669bb9', 'Yost-Lindgren', 'Diena Bunstone', 'dbunstone2@springer.com', '115-139-6853');
insert into organization (organization_id, name, contact_name, contact_email, contact_phone) values ('71beaa3b-84a7-4150-baf2-5fc3d0142b24', 'Goodwin LLC', 'Adlai Edelheid', 'aedelheid3@utexas.edu', '796-860-0581');
insert into organization (organization_id, name, contact_name, contact_email, contact_phone) values ('f950f0ee-aab5-4cc1-b601-c71e512f2092', 'Hessel and Sons', 'Nedi Wimmer', 'nwimmer4@imdb.com', '140-491-2655');

INSERT INTO public.license(license_id, organization_id, description,
                            product_name, license_type, comment)
VALUES
    ('f2a9c9d4-d2c0-44fa-97fe-724d77173c62', '71beaa3b-84a7-4150-baf2-5fc3d0142b24', 'Software Product',
     'Goodwin LLC', 'complete', 'I AM DEV'),
    ('279709ff-e6d5-4a54-8b55-a5c37542025b', 'f950f0ee-aab5-4cc1-b601-c71e512f2092', 'Software Product',
     'Hessel and Sons', 'complete', 'I AM DEV');