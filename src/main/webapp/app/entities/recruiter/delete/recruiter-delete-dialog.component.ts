import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRecruiter } from '../recruiter.model';
import { RecruiterService } from '../service/recruiter.service';

@Component({
  standalone: true,
  templateUrl: './recruiter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RecruiterDeleteDialogComponent {
  recruiter?: IRecruiter;

  protected recruiterService = inject(RecruiterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recruiterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
