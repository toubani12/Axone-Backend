import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVAchievement } from '../tech-cv-achievement.model';
import { TechCVAchievementService } from '../service/tech-cv-achievement.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-achievement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVAchievementDeleteDialogComponent {
  techCVAchievement?: ITechCVAchievement;

  protected techCVAchievementService = inject(TechCVAchievementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVAchievementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
