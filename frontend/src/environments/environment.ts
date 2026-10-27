// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  // Relative paths go through proxy.conf.json → http://localhost:8080
  apiBaseUrl: '/api',
  // Empty: WebSocket uses the current host (ng serve proxies /ws → :8080)
  wsBaseUrl: '',
  firebase: {
    apiKey: 'AIzaSyDHgUVpSiMhc6LjujfjC5NhiCi07vPqmnU',
    authDomain: 'sofkau-heroes.firebaseapp.com',
    projectId: 'sofkau-heroes',
    storageBucket: 'sofkau-heroes.appspot.com',
    messagingSenderId: '162611560419',
    appId: '1:162611560419:web:88724e642e2b9c838eead6',
  },
};
