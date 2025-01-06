import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITechnicalCV } from 'app/entities/technical-cv/technical-cv.model';
import { TechnicalCVService } from 'app/entities/technical-cv/service/technical-cv.service';
import { ITechCVAltActivities } from '../tech-cv-alt-activities.model';
import { TechCVAltActivitiesService } from '../service/tech-cv-alt-activities.service';
import { TechCVAltActivitiesFormService, TechCVAltActivitiesFormGroup } from './tech-cv-alt-activities-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tech-cv-alt-activities-update',
  templateUrl: './tech-cv-alt-activities-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TechCVAltActivitiesUpdateComponent implements OnInit {
  isSaving = false;
  techCVAltActivities: ITechCVAltActivities | null = null;

  technicalCVSSharedCollection: ITechnicalCV[] = [];

  protected techCVAltActivitiesService = inject(TechCVAltActivitiesService);
  protected techCVAltActivitiesFormService = inject(TechCVAltActivitiesFormService);
  protected technicalCVService = inject(TechnicalCVService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TechCVAltActivitiesFormGroup = this.techCVAltActivitiesFormService.createTechCVAltActivitiesFormGroup();

  compareTechnicalCV = (o1: ITechnicalCV | null, o2: ITechnicalCV | null): boolean => this.technicalCVService.compareTechnicalCV(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techCVAltActivities }) => {
      this.techCVAltActivities = techCVAltActivities;
      if (techCVAltActivities) {
        this.updateForm(techCVAltActivities);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const techCVAltActivities = this.techCVAltActivitiesFormService.getTechCVAltActivities(this.editForm);
    if (techCVAltActivities.id !== null) {
      this.subscribeToSaveResponse(this.techCVAltActivitiesService.update(techCVAltActivities));
    } else {
      this.subscribeToSaveResponse(this.techCVAltActivitiesService.create(techCVAltActivities));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechCVAltActivities>>): void {
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

  protected updateForm(techCVAltActivities: ITechCVAltActivities): void {
    this.techCVAltActivities = techCVAltActivities;
    this.techCVAltActivitiesFormService.resetForm(this.editForm, techCVAltActivities);

    this.technicalCVSSharedCollection = this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(
      this.technicalCVSSharedCollection,
      techCVAltActivities.technicalCV,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.technicalCVService
      .query()
      .pipe(map((res: HttpResponse<ITechnicalCV[]>) => res.body ?? []))
      .pipe(
        map((technicalCVS: ITechnicalCV[]) =>
          this.technicalCVService.addTechnicalCVToCollectionIfMissing<ITechnicalCV>(technicalCVS, this.techCVAltActivities?.technicalCV),
        ),
      )
      .subscribe((technicalCVS: ITechnicalCV[]) => (this.technicalCVSSharedCollection = technicalCVS));
  }
}
