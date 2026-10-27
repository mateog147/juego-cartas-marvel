import { environment } from './environment';

/** REST base, e.g. `/api` (local) or `https://host/api` (prod). */
export function apiUrl(path: string): string {
  const normalized = path.startsWith('/') ? path : `/${path}`;
  return `${environment.apiBaseUrl}${normalized}`;
}

/**
 * WebSocket base + path.
 * Local (empty wsBaseUrl): same host as the page so ng serve can proxy `/ws`.
 * Prod: absolute `wss://.../ws` from environment.prod.ts.
 */
export function wsUrl(path: string): string {
  const normalized = path.startsWith('/') ? path : `/${path}`;
  if (environment.wsBaseUrl) {
    return `${environment.wsBaseUrl}${normalized}`;
  }
  const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${protocol}//${window.location.host}/ws${normalized}`;
}
