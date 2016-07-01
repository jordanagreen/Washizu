# Washizu
An Android mahjong app I'm working on in my spare time. As the name implies, the goal is to have the option to play Washizu Mahjong, with transparent tiles. Currently I'm only focusing on playing single-player against AI opponents, but since Washizu is really meant to be 2 on 2, some form of online multiplayer would definitely be worth looking into adding once I'm finished.

Right now there isn't much to play, since the AI just discards randomly and calls on everything it can. The basic discard and draw is there, plus turn order changing when calls are made, so I'm planning on finishing the basic game features before doing anything more fancy.

### Tentative to-do list:

1. Write a class to check if a hand has won and score it
  * ~~Write a class to split a hand into melds and a pair (i.e. see if it won)~~
  * ~~Check for all the different yaku~~ Got most of them done, will do the rest later
2. Finish implementing ~~open/~~closed kan
3. Implement ~~ron/~~tsumo
~~4. Add buttons to decide whether to call or not~~
5. Make some test games for each yaku to make sure they win right
6. Modify a pre-existing AI to work with my logic
7. Improve the UI
8. Add regular/Washizu option
9. Possible multiplayer in the future
