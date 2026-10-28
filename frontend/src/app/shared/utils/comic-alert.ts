import Swal, { SweetAlertIcon, SweetAlertOptions, SweetAlertResult } from 'sweetalert2';

const BASE: SweetAlertOptions = {
  customClass: {
    popup: 'comic-swal',
    title: 'comic-swal-title',
    htmlContainer: 'comic-swal-text',
    confirmButton: 'comic-swal-confirm',
    cancelButton: 'comic-swal-cancel',
    timerProgressBar: 'comic-swal-timer',
  },
  buttonsStyling: false,
  background: '#12141c',
  color: '#fff7e0',
  confirmButtonText: 'OK',
};

export function comicAlert(options: SweetAlertOptions | string): Promise<SweetAlertResult> {
  if (typeof options === 'string') {
    return Swal.fire({
      ...BASE,
      title: options,
    });
  }
  const baseClasses = (BASE.customClass || {}) as Record<string, string>;
  const optionClasses = (options.customClass || {}) as Record<string, string>;
  return Swal.fire({
    ...BASE,
    ...options,
    customClass: {
      ...baseClasses,
      ...optionClasses,
    },
  });
}

export function comicWin(title: string, subtitle?: string, opts: SweetAlertOptions = {}): Promise<SweetAlertResult> {
  return comicAlert({
    title,
    html: `
      <div class="comic-swal-burst">POW!</div>
      ${subtitle ? `<p class="comic-swal-sub">${subtitle}</p>` : ''}
    `,
    confirmButtonText: 'Seguir',
    ...opts,
  });
}

export function comicInfo(title: string, text?: string): Promise<SweetAlertResult> {
  return comicAlert({
    title,
    text,
    confirmButtonText: 'Entendido',
  });
}

export function comicConfirm(title: string, text?: string): Promise<SweetAlertResult> {
  return comicAlert({
    title,
    text,
    showCancelButton: true,
    confirmButtonText: 'Sí',
    cancelButtonText: 'No',
  });
}

export function comicError(title: string, text?: string): Promise<SweetAlertResult> {
  return comicAlert({
    title,
    text,
    icon: 'error' as SweetAlertIcon,
    confirmButtonText: 'Cerrar',
  });
}
