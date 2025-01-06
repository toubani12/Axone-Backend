import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVProject } from '../tech-cv-project.model';
import { TechCVProjectService } from '../service/tech-cv-project.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-project-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVProjectDeleteDialogComponent {
  techCVProject?: ITechCVProject;

  protected techCVProjectService = inject(TechCVProjectService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVProjectService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
