import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechnicalCV } from '../technical-cv.model';
import { TechnicalCVService } from '../service/technical-cv.service';

@Component({
  standalone: true,
  templateUrl: './technical-cv-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechnicalCVDeleteDialogComponent {
  technicalCV?: ITechnicalCV;

  protected technicalCVService = inject(TechnicalCVService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.technicalCVService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
