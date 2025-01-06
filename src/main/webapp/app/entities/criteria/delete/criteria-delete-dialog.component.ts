import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICriteria } from '../criteria.model';
import { CriteriaService } from '../service/criteria.service';

@Component({
  standalone: true,
  templateUrl: './criteria-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CriteriaDeleteDialogComponent {
  criteria?: ICriteria;

  protected criteriaService = inject(CriteriaService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.criteriaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
