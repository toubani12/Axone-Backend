import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAppTest } from '../app-test.model';
import { AppTestService } from '../service/app-test.service';

@Component({
  standalone: true,
  templateUrl: './app-test-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AppTestDeleteDialogComponent {
  appTest?: IAppTest;

  protected appTestService = inject(AppTestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appTestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
