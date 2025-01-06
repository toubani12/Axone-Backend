import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProvider } from '../provider.model';
import { ProviderService } from '../service/provider.service';

@Component({
  standalone: true,
  templateUrl: './provider-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProviderDeleteDialogComponent {
  provider?: IProvider;

  protected providerService = inject(ProviderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.providerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
