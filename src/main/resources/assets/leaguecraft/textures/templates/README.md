# Texture templates

Ces PNG sont des patrons UV pour peindre les textures des modeles LeagueCraft.
Ils ne sont pas utilises par le jeu directement : copie un fichier template, peins dessus, puis remplace la texture finale correspondante.

Les patrons n'ont pas d'ecriture pour rester lisibles dans un editeur de pixel art.
Chaque rectangle correspond a une face UV d'un cube du modele.
Le fond est transparent : tu peux ajouter ton propre calque de fond dans ton editeur si tu veux plus de reperes.

## Correspondance rapide

- minion_template.png -> textures/entity/minion/*_minion.png
- caster_minion_template.png -> futur texture caster dediee si tu veux en ajouter une
- cannon_minion_template.png -> futur texture canon dediee si tu veux en ajouter une
- blue_super_minion_template.png -> textures/entity/minion/blue_super_minion.png
- red_super_minion_template.png -> textures/entity/minion/red_super_minion.png
- blue_buff_template.png -> textures/entity/jungle/blue_buff.png
- red_buff_template.png -> textures/entity/jungle/red_buff.png
- void_grub_template.png -> textures/entity/jungle/void_grub.png
- rift_herald_template.png -> textures/entity/jungle/rift_herald.png
- gromp_template.png -> textures/entity/jungle/gromp.png
- krug_template.png -> textures/entity/jungle/krug.png
- raptor_template.png -> textures/entity/jungle/raptor.png
- dragon_template.png -> textures/entity/jungle/dragon.png
- baron_nashor_template.png -> textures/entity/jungle/baron_nashor.png
- block_face_16_template.png -> base 16x16 pour les textures cube_all de blocs

## Code couleur

- Jaune : face du dessus
- Orange : face du dessous
- Bleu : face avant
- Vert : face arriere
- Violet : face gauche
- Rose : face droite
- Teinte legerement differente : autre piece du modele, par exemple tete, corps, bras, patte, queue, canon, baton, etc.
- Bord clair : limite exacte de la zone a peindre

Garde exactement la meme taille d'image que le patron quand tu exportes ta texture finale.
