import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TechCVLevel } from 'app/entities/enumerations/tech-cv-level.model';
import { ITechnicalCV } from '../technical-cv.model';
import { TechnicalCVService } from '../service/technical-cv.service';
import { TechnicalCVFormService, TechnicalCVFormGroup } from './technical-cv-form.service';

@Component({
  standalone: true,
  selector: 'jhi-technical-cv-update',
  templateUrl: './technical-cv-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechnicalCVUpdateComponent implements OnInit {
  isSaving = false;
  technicalCV: ITechnicalCV | null = null;
  techCVLevelValues = Object.keys(TechCVLevel);

  protected technicalCVService = inject(TechnicalCVService);
  protected technicalCVFormService = inject(TechnicalCVFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechnicalCVFormGroup = this.technicalCVFormService.createTechnicalCVFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ technicalCV }) => {
      this.technicalCV = technicalCV;
      if (technicalCV) {
        this.updateForm(technicalCV);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const technicalCV = this.technicalCVFormService.getTechnicalCV(this.editForm);
    if (technicalCV.id !== null) {
      this.subscribeToSaveResponse(this.technicalCVService.update(technicalCV));
    } else {
      this.subscribeToSaveResponse(this.technicalCVService.create(technicalCV));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnicalCV>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(technicalCV: ITechnicalCV): void {
    this.technicalCV = technicalCV;
    this.technicalCVFormService.resetForm(this.editForm, technicalCV);
  }
}
