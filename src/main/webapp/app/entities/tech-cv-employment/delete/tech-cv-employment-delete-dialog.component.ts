import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVEmployment } from '../tech-cv-employment.model';
import { TechCVEmploymentService } from '../service/tech-cv-employment.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-employment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVEmploymentDeleteDialogComponent {
  techCVEmployment?: ITechCVEmployment;

  protected techCVEmploymentService = inject(TechCVEmploymentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVEmploymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
