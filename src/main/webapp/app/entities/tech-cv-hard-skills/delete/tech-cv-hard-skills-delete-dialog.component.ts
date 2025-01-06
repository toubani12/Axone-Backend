import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVHardSkills } from '../tech-cv-hard-skills.model';
import { TechCVHardSkillsService } from '../service/tech-cv-hard-skills.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-hard-skills-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVHardSkillsDeleteDialogComponent {
  techCVHardSkills?: ITechCVHardSkills;

  protected techCVHardSkillsService = inject(TechCVHardSkillsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVHardSkillsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
