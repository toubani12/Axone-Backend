import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITechCVSoftSkills } from '../tech-cv-soft-skills.model';
import { TechCVSoftSkillsService } from '../service/tech-cv-soft-skills.service';

@Component({
  standalone: true,
  templateUrl: './tech-cv-soft-skills-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TechCVSoftSkillsDeleteDialogComponent {
  techCVSoftSkills?: ITechCVSoftSkills;

  protected techCVSoftSkillsService = inject(TechCVSoftSkillsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.techCVSoftSkillsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
