import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICustomQuestion } from '../custom-question.model';
import { CustomQuestionService } from '../service/custom-question.service';

@Component({
  standalone: true,
  templateUrl: './custom-question-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CustomQuestionDeleteDialogComponent {
  customQuestion?: ICustomQuestion;

  protected customQuestionService = inject(CustomQuestionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customQuestionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
