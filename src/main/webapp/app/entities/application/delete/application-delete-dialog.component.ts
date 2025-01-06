import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IApplication } from '../application.model';
import { ApplicationService } from '../service/application.service';

@Component({
  standalone: true,
  templateUrl: './application-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ApplicationDeleteDialogComponent {
  application?: IApplication;

  protected applicationService = inject(ApplicationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.applicationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
