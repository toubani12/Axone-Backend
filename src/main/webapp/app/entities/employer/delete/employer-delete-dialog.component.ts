import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEmployer } from '../employer.model';
import { EmployerService } from '../service/employer.service';

@Component({
  standalone: true,
  templateUrl: './employer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EmployerDeleteDialogComponent {
  employer?: IEmployer;

  protected employerService = inject(EmployerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
