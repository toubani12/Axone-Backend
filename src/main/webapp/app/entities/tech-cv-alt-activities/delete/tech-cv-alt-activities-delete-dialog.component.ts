import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';
import { TechCVAltActivitiesService } from '../service/tech-cv-alt-activities.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-alt-activities-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVAltActivitiesDeleteDialogComponent {
  techCVAltActivities?: ITechCVAltActivities;

  protected techCVAltActivitiesService = inject(TechCVAltActivitiesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVAltActivitiesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
