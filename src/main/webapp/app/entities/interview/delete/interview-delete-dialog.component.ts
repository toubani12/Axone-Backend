import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInterview } from '../interview.model';
import { InterviewService } from '../service/interview.service';

@Component({
  standalone: true,
  templateUrl: './interview-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InterviewDeleteDialogComponent {
  interview?: IInterview;

  protected interviewService = inject(InterviewService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interviewService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
