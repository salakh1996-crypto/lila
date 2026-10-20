const make = (name: string, volume?: number) => {
  site.sound.load(name, site.sound.url(`${name}.mp3`));
  return () => site.sound.play(name, volume);
};

export const move = () => site.sound.play('move');
export const take = make('sfx/Tournament3rd', 0.4);
export const levelStart = make('sfx/GenericNotify');
export const levelEnd = make('sfx/Confirmation');
export const stageStart = make('sfx/NewChallenge');
export const stageEnd = make('sfx/Victory');
export const failure = make('sfx/Error');
