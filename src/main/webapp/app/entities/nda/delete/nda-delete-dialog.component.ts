import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INDA } from '../nda.model';
import { NDAService } from '../service/nda.service';

@Component({
  standalone: true,
  templateUrl: './nda-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NDADeleteDialogComponent {
  nDA?: INDA;

  protected nDAService = inject(NDAService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nDAService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
