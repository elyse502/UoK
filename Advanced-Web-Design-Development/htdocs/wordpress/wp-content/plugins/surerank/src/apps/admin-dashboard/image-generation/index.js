import PageContentWrapper from '@/apps/admin-components/page-content-wrapper';
import { UpgradeNotice } from '@/global/components/nudges';
import { __ } from '@wordpress/i18n';

/**
 * Image Generation Settings - Pro Feature Placeholder
 * Displays upgrade nudge for the Image Generation feature.
 *
 * @since x.x.x
 */
const ImageGenerationUpgrade = () => {
	return (
		<PageContentWrapper
			title={ __( 'Image Generation', 'surerank' ) }
			description={ __(
				'Automatically generate Open Graph images for social sharing of your WordPress posts and pages.',
				'surerank'
			) }
		>
			<UpgradeNotice
				title={ __( 'Upgrade to unlock Image Generation', 'surerank' ) }
				plan="pro"
				description={ __(
					'Upgrade to SureRank Pro or Business and generate stunning AI-powered Open Graph images with custom branding presets for your posts and pages.',
					'surerank'
				) }
				utmMedium="surerank_og_image_generation"
			/>
		</PageContentWrapper>
	);
};

export default ImageGenerationUpgrade;
