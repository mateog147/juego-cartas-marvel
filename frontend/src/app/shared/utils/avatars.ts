export const AVATAR_PRESETS: string[] = [
  'assets/character-default.png',
  'assets/character-0.png',
  'assets/character-1.png',
  'assets/character-2.png',
  'assets/character-3.png',
  'assets/character-4.png',
  'assets/character-5.png',
];

export function resolveAvatar(avatar?: string | null, photoURL?: string | null): string {
  if (avatar && AVATAR_PRESETS.includes(avatar)) {
    return avatar;
  }
  if (photoURL && (photoURL.startsWith('assets/') || photoURL.startsWith('http'))) {
    return photoURL;
  }
  return AVATAR_PRESETS[0];
}
