/** Map seeded hero names → local comic cover art under assets/imgs. */
const ART_BY_NAME: Record<string, string> = {
  hulk: 'hulk.jpg',
  spiderman: 'hombrearania2099.jpg',
  ironman: 'ironman.jpg',
  thor: 'diosdeltrueno.jpg',
  captainamerica: 'capitanamerica.jpg',
  blackwidow: 'gatanegra.jpg',
  doctorstrange: 'extranio.jpg',
  scarletwitch: 'labrujaescarlata.jpg',
  loki: 'mephisto.jpg',
  thanos: 'thanos.jpg',
  wolverine: 'lobezno.jpg',
  storm: 'fuerzax.jpg',
  deadpool: 'maximumcarnage.jpg',
  capmarvel: 'capvscap.jpg',
  antman: 'invasores.jpg',
  vision: 'extranio.jpg',
  hawkeye: 'bloodhawk.jpg',
  groot: 'elnuevonova.jpg',
  rocket: 'ghost.jpg',
  punisher: 'punisher.jpg',
};

const FALLBACK = 'boomboom.jpg';

function normalize(name: string): string {
  return (name || '')
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-z0-9]/g, '');
}

/** Resolve a usable image URL for a card (local asset preferred over placeholders). */
export function resolveCardArt(nombre: string, imagen?: string | null): string {
  const key = normalize(nombre);
  const file = ART_BY_NAME[key];
  if (file) {
    return `assets/imgs/${file}`;
  }
  if (imagen && !imagen.includes('via.placeholder.com') && !imagen.includes('placehold')) {
    if (imagen.startsWith('http') || imagen.startsWith('assets/') || imagen.startsWith('/')) {
      return imagen;
    }
    return `assets/imgs/${imagen}`;
  }
  return `assets/imgs/${FALLBACK}`;
}
