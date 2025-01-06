import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVEducation } from '../tech-cv-education.model';
import { TechCVEducationService } from '../service/tech-cv-education.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-education-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVEducationDeleteDialogComponent {
  techCVEducation?: ITechCVEducation;

  protected techCVEducationService = inject(TechCVEducationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVEducationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
