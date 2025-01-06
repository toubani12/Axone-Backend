import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResume } from '../resume.model';
import { ResumeService } from '../service/resume.service';

@Component({
  standalone: true,
  templateUrl: './resume-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResumeDeleteDialogComponent {
  resume?: IResume;

  protected resumeService = inject(ResumeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resumeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
